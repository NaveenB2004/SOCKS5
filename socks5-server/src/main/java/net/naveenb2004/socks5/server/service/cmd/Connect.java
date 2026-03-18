/*
 * Copyright (c) 2026 Naveen N. Balasooriya
 *
 * This source code is licensed under the MIT License.
 * See the LICENSE file in the project root for full license information.
 */

package net.naveenb2004.socks5.server.service.cmd;

import net.naveenb2004.socks5.base.AddressType;
import net.naveenb2004.socks5.base.Reply;
import net.naveenb2004.socks5.base.template.CmdRequest;
import net.naveenb2004.socks5.base.template.CmdResponse;
import net.naveenb2004.socks5.server.configuration.SOCKS5ServerConfig;
import net.naveenb2004.socks5.server.exception.SOCKS5ServerException;
import net.naveenb2004.socks5.server.service.ServerCmdService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;

public final class Connect extends CmdHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(Connect.class);

    private final Socket clientSocket;
    private final CmdRequest clientRequest;
    private final SOCKS5ServerConfig serverConfig;

    public Connect(final Socket clientSocket,
                   final CmdRequest clientRequest,
                   final SOCKS5ServerConfig serverConfig) throws IOException {
        this.clientSocket = clientSocket;
        this.clientRequest = clientRequest;
        this.serverConfig = serverConfig;
    }

    @Override
    public void handle() throws IOException, InterruptedException {
        final var destination = CmdHandler.getDestination(clientRequest, clientSocket.getOutputStream());
        LOGGER.atDebug().log("Sending request to: {}", destination);
        try (final var destSocket = buildSocket(destination)) {
            sendSuccessResp(destSocket);
            final var countDownLatch = new CountDownLatch(2);
            serverConfig.getConcurrentThreadFactory().newThread(() -> {
                try {
                    wireIO(clientSocket.getInputStream(), destSocket.getOutputStream());
                    destSocket.shutdownOutput();
                    countDownLatch.countDown();
                } catch (IOException e) {
                    throw new SOCKS5ServerException(e);
                }
            }).start();
            serverConfig.getConcurrentThreadFactory().newThread(() -> {
                try {
                    wireIO(destSocket.getInputStream(), clientSocket.getOutputStream());
                    clientSocket.shutdownOutput();
                    countDownLatch.countDown();
                } catch (IOException e) {
                    throw new SOCKS5ServerException(e);
                }
            }).start();
            countDownLatch.await();
        }
    }

    private Socket buildSocket(final InetSocketAddress destination) throws IOException {
        final var socket = new Socket();
        socket.connect(destination);
        socket.setTcpNoDelay(true);
        return socket;
    }

    private void sendSuccessResp(final Socket destSocket) throws IOException {
        final var localAddr = clientSocket.getLocalAddress();
        final var addrType = localAddr instanceof Inet4Address ? AddressType.IPv4 : AddressType.IPv6;
        final var cmdResp = new CmdResponse(Reply.SUCCEEDED, addrType, localAddr.getAddress(), destSocket.getLocalPort());
        ServerCmdService.sendCmdResp(clientSocket.getOutputStream(), cmdResp);
    }

    private void wireIO(final InputStream inputStream,
                        final OutputStream outputStream) throws IOException {
        int byteCount;
        byte[] buffer = new byte[serverConfig.getInternalBufferSize()];
        while ((byteCount = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, byteCount);
            outputStream.flush();
        }
    }
}
