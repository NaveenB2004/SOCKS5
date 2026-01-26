package io.github.naveenb2004.socks5.server.service.reqHandler;

import io.github.naveenb2004.socks5.base.util.SocketBuilder;
import io.github.naveenb2004.socks5.server.config.SOCKS5ServerConfiguration;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.ThreadFactory;

public final class ConnectReqHandler implements ReqHandler {
    private final Socket srcSocket;
    private final Socket destSocket;
    private final ThreadFactory threadFactory;

    public ConnectReqHandler(Socket srcSocket,
                             InetAddress dstAddr,
                             int dstPort,
                             SOCKS5ServerConfiguration config) {
        this.srcSocket = srcSocket;
        this.destSocket = config.isSslEnabledForClients() ?
                SocketBuilder.build(
                        dstAddr,
                        dstPort,
                        config.getSslContextProtocol(),
                        config.getSslContextProtocolProvider(),
                        config.getKeyManagers(),
                        config.getTrustManagers(),
                        config.getSecureRandom(),
                        config.getSslParameters()) :
                SocketBuilder.build(dstAddr, dstPort);
        this.threadFactory = config.getThreadFactory();
    }

    @Override
    public void init() {

    }

    @Override
    public void handle() {
        threadFactory.newThread(() -> {
            bind(srcSocket, destSocket);
        }).start();
        bind(destSocket, srcSocket);
    }

    private void bind(Socket destSocket,
                      Socket srcSocket) {
        try {
            InputStream inputStream = destSocket.getInputStream();
            OutputStream outputStream = srcSocket.getOutputStream();
            int c;
            byte[] buffer = new byte[1024];
            while ((c = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, c);
                outputStream.flush();
            }
        } catch (IOException e) {
            try {
                if (!srcSocket.isClosed()) srcSocket.close();
                if (!destSocket.isClosed()) destSocket.close();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
