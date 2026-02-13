package io.github.naveenb2004.socks5.server.command.processor;

import io.github.naveenb2004.socks5.base.ATYP;
import io.github.naveenb2004.socks5.base.REP;
import io.github.naveenb2004.socks5.server.exception.SOCKS5ServerException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public final class ConnectProcessor extends CommandProcessor {
    private Socket dstSocket;

    public ConnectProcessor(Socket socks5Client,
                            ATYP dstAtyp,
                            InetSocketAddress dst) {
        super(socks5Client, dstAtyp, dst);
    }

    @Override
    public void process() {
        try {
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

            Thread.ofVirtual().start(() -> {
                try {
                    wireStream(dstIn, srcOut);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            wireStream(srcIn, dstOut);
        } catch (Exception e) {
            throw new SOCKS5ServerException(e);
        }
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
            byte[] buffer = new byte[10_240];
            while ((b = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, b);
                outputStream.flush();
            }
            outputStream.close();
        } catch (IOException e) {
            throw new SOCKS5ServerException(e);
        }
    }
}
