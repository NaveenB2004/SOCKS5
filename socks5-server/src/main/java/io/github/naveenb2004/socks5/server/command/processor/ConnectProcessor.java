package io.github.naveenb2004.socks5.server.command.processor;

import io.github.naveenb2004.socks5.base.ATYP;
import io.github.naveenb2004.socks5.base.REP;
import io.github.naveenb2004.socks5.base.SOCKS5Properties;
import io.github.naveenb2004.socks5.server.exception.SOCKS5ServerException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class ConnectProcessor extends CommandProcessor {
    private Socket dstSocket;
    private final ExecutorService executor;

    public ConnectProcessor(Socket socks5Client,
                            ATYP dstAtyp,
                            InetSocketAddress dst) {
        super(socks5Client, dstAtyp, dst);
        executor = Executors.newFixedThreadPool(2, Thread.ofPlatform().factory());
    }

    @Override
    public void process() throws IOException {
        initRemote();
        super.sendResponse(REP.SUCCEEDED);

        System.out.println(
                "From: " + socks5Client.getInetAddress() + " : " + socks5Client.getPort() + " | " +
                        "To: " + dstSocket.getInetAddress() + " : " + dstSocket.getPort()
                          );

        InputStream srcIn = super.socks5Client.getInputStream();
        OutputStream srcOut = super.socks5Client.getOutputStream();
        InputStream dstIn = dstSocket.getInputStream();
        OutputStream dstOut = dstSocket.getOutputStream();

        executor.execute(() -> {
            try {
                wireStream(dstIn, srcOut);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        executor.execute(() -> {
            try {
                wireStream(srcIn, dstOut);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void initRemote() throws IOException {
        try {
            this.dstSocket = new Socket();
            dstSocket.connect(dst);

            super.bndSocketAddr = (InetSocketAddress) dstSocket.getLocalSocketAddress();
            super.bndAtyp = super.bndSocketAddr.getAddress() instanceof Inet4Address ? ATYP.IP_V4_ADDRESS : ATYP.IP_V6_ADDRESS;
        } catch (UnknownHostException e) {
            super.sendResponse(REP.HOST_UNREACHABLE);
            throw e;
        } catch (Exception e) {
            super.sendResponse(REP.GENERAL_SOCKS_SERVER_FAILURE);
            throw e;
        }
    }

    private void wireStream(InputStream inputStream,
                            OutputStream outputStream) throws IOException {
        try {
            int b;
            byte[] buffer = new byte[SOCKS5Properties.INTERNAL_BUFFER_SIZE];
            while ((b = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, b);
            }
        } catch (IOException e) {
            throw new SOCKS5ServerException(e);
        }
    }
}
