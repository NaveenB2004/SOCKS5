package io.github.naveenb2004.socks5.server.command.processor;

import io.github.naveenb2004.socks5.base.ATYP;
import io.github.naveenb2004.socks5.base.CMD;
import io.github.naveenb2004.socks5.base.REP;
import io.github.naveenb2004.socks5.server.config.SOCKS5Ruleset;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

public abstract sealed class CommandProcessor
        permits BindProcessor, ConnectProcessor, UdpAssociateProcessor {
    protected Socket socks5Client;

    protected ATYP atyp;
    protected byte[] bndAddr;
    protected byte[] bndPort;

    public abstract void process();

    protected void sendResponse(REP rep) throws IOException {
        OutputStream outputStream = socks5Client.getOutputStream();
        outputStream.write(0x05);
        outputStream.write(rep.getValue());
        outputStream.write(0x00);
        outputStream.write(atyp.getValue());
        outputStream.write(bndAddr);
        outputStream.write(bndPort);
    }
}
