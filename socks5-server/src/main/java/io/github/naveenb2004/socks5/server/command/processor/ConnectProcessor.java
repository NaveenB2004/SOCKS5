package io.github.naveenb2004.socks5.server.command.processor;

import io.github.naveenb2004.socks5.base.ATYP;
import io.github.naveenb2004.socks5.base.REP;
import io.github.naveenb2004.socks5.server.exception.SOCKS5ServerException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

            InputStream srcIn = super.socks5Client.getInputStream();
            OutputStream srcOut = super.socks5Client.getOutputStream();
            InputStream dstIn = dstSocket.getInputStream();
            OutputStream dstOut = dstSocket.getOutputStream();

            wireStream(srcIn, dstOut);
            Thread.ofVirtual().start(() -> wireStream(dstIn, srcOut));
        } catch (Exception e) {
            throw new SOCKS5ServerException(e);
        }
    }

    private void initRemote() throws IOException {
        try (Socket dstSocket = new Socket()) {
            dstSocket.connect(dst);
            this.dstSocket = dstSocket;
            super.bndSocketAddr = (InetSocketAddress) dstSocket.getLocalSocketAddress();
        } catch (UnknownHostException e) {
            super.sendResponse(REP.HOST_UNREACHABLE);
            throw e;
        } catch (Exception e) {
            super.sendResponse(REP.GENERAL_SOCKS_SERVER_FAILURE);
            throw e;
        }
    }

    private void wireStream(InputStream srcIn,
                            OutputStream dstOut) {
        try {
            int bytesRead;
            byte[] buffer = new byte[1024];
            while ((bytesRead = srcIn.read(buffer)) != -1) {
                dstOut.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            try {
                if (!super.socks5Client.isClosed()) super.socks5Client.close();
                if (!dstSocket.isClosed()) dstSocket.close();
            } catch (IOException ex) {
                throw new SOCKS5ServerException(ex);
            }
            throw new SOCKS5ServerException(e);
        }
    }
}
