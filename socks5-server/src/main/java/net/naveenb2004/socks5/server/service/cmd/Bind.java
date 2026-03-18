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
import net.naveenb2004.socks5.server.service.core.ServerCmdService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

public final class Bind extends CmdHandler {
    private final Socket clientSocket;
    private final CmdRequest clientRequest;
    private final SOCKS5ServerConfig serverConfig;

    public Bind(final Socket clientSocket,
                final CmdRequest clientRequest,
                final SOCKS5ServerConfig serverConfig) throws IOException {
        this.clientSocket = clientSocket;
        this.clientRequest = clientRequest;
        this.serverConfig = serverConfig;
    }

    @Override
    public void handle() throws IOException, InterruptedException {
        try (final Socket incomingSocket = acceptIncomingSocket()) {
            incomingSocket.setTcpNoDelay(true);

            var addrType = incomingSocket.getInetAddress() instanceof Inet4Address ? AddressType.IPv4 : AddressType.IPv6;
            var bindResp2 = new CmdResponse(Reply.SUCCEEDED, addrType, incomingSocket.getInetAddress().getAddress(), incomingSocket.getPort());
            ServerCmdService.sendCmdResp(clientSocket.getOutputStream(), bindResp2);

            final var countDownLatch = new CountDownLatch(2);
            serverConfig.getConcurrentThreadFactory().newThread(() -> {
                try {
                    wireIO(clientSocket.getInputStream(), incomingSocket.getOutputStream());
                    incomingSocket.shutdownOutput();
                    countDownLatch.countDown();
                } catch (IOException e) {
                    throw new SOCKS5ServerException(e);
                }
            }).start();
            serverConfig.getConcurrentThreadFactory().newThread(() -> {
                try {
                    wireIO(incomingSocket.getInputStream(), clientSocket.getOutputStream());
                    clientSocket.shutdownOutput();
                    countDownLatch.countDown();
                } catch (IOException e) {
                    throw new SOCKS5ServerException(e);
                }
            }).start();
            countDownLatch.await();
        }
    }

    private Socket acceptIncomingSocket() throws IOException {
        try (var serverSocket = new ServerSocket(0)) {
            serverSocket.setReceiveBufferSize(serverConfig.getInternalBufferSize());
            serverSocket.setSoTimeout(serverConfig.getConnectionTimeout());

            var addrType = serverSocket.getInetAddress() instanceof Inet4Address ? AddressType.IPv4 : AddressType.IPv6;
            var bindResp1 = new CmdResponse(Reply.SUCCEEDED, addrType, serverSocket.getInetAddress().getAddress(), serverSocket.getLocalPort());
            ServerCmdService.sendCmdResp(clientSocket.getOutputStream(), bindResp1);

            int i = 0;
            final var wildcardAddr = InetAddress.getByName(clientRequest.addressType() == AddressType.IPv4 ? "0" : "::");
            while (!serverSocket.isClosed() && (i++) < serverConfig.getMaxBindReqCycles()) {
                var incomingSocket = serverSocket.accept();
                if (!Arrays.equals(clientRequest.destAddress(), wildcardAddr.getAddress()) &&
                        !Arrays.equals(clientRequest.destAddress(), incomingSocket.getInetAddress().getAddress()) ||
                        clientRequest.destPort() != 0 && clientRequest.destPort() != incomingSocket.getPort()) {
                    incomingSocket.close();
                    continue;
                }
                return incomingSocket;
            }
        }

        var failResp = new CmdResponse(Reply.HOST_UNREACHABLE, AddressType.DOMAIN_NAME, new byte[]{0x00}, 0);
        ServerCmdService.sendCmdResp(clientSocket.getOutputStream(), failResp);
        throw new SOCKS5ServerException("Client bind failed");
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
