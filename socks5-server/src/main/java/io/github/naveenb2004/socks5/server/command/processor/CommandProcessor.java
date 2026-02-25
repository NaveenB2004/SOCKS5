package io.github.naveenb2004.socks5.server.command.processor;

import io.github.naveenb2004.socks5.base.ATYP;
import io.github.naveenb2004.socks5.base.REP;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public abstract sealed class CommandProcessor
        permits BindProcessor, ConnectProcessor, UdpAssociateProcessor {
    protected final Socket socks5Client;
    protected final ATYP dstAtyp;
    protected final InetSocketAddress dst;

    protected ATYP bndAtyp;
    protected InetSocketAddress bndSocketAddr;

    public CommandProcessor(Socket socks5Client,
                            ATYP dstAtyp,
                            InetSocketAddress dst) {
        this.socks5Client = socks5Client;
        this.dstAtyp = dstAtyp;
        this.dst = dst;
    }

    public abstract void process() throws IOException;

    public void sendResponse(REP rep) throws IOException {
        OutputStream outputStream = socks5Client.getOutputStream();
        outputStream.write(0x05);
        outputStream.write(rep.getValue());
        outputStream.write(0x00);
        if (rep == REP.SUCCEEDED) {
            outputStream.write(bndAtyp.getValue());
            outputStream.write(bndSocketAddr.getAddress().getAddress());
            outputStream.write(bndSocketAddr.getPort() >>> 8);
            outputStream.write(bndSocketAddr.getPort() & 0xFF);
        } else {
            outputStream.write(ATYP.IP_V4_ADDRESS.getValue());
            outputStream.write(new byte[]{0, 0, 0, 0});
            outputStream.write(new byte[]{0, 0});
        }
    }
}
