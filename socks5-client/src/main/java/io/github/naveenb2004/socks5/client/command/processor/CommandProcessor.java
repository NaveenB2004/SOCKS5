package io.github.naveenb2004.socks5.client.command.processor;

import io.github.naveenb2004.socks5.base.ATYP;
import io.github.naveenb2004.socks5.base.CMD;
import io.github.naveenb2004.socks5.client.command.SOCKS5Response;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public abstract sealed class CommandProcessor
        permits BindProcessor, ConnectProcessor, UdpAssociateProcessor {
    protected final Socket socks5Server;
    protected final CMD cmd;
    protected final ATYP atyp;
    protected final byte[] dstAddr;
    protected final byte[] dstPort;

    protected CommandProcessor(Socket socks5Server,
                               CMD cmd,
                               ATYP atyp,
                               byte[] dstAddr,
                               byte[] dstPort) {
        this.socks5Server = socks5Server;
        this.cmd = cmd;
        this.atyp = atyp;
        this.dstAddr = dstAddr;
        this.dstPort = dstPort;
    }

    public abstract SOCKS5Response process();

    protected void sendRequest() throws IOException {
        OutputStream outputStream = socks5Server.getOutputStream();
        outputStream.write(0x05);
        outputStream.write(cmd.getValue());
        outputStream.write(0x00);
        outputStream.write(atyp.getValue());
        switch (atyp) {
            case IP_V4_ADDRESS, IP_V6_ADDRESS -> outputStream.write(dstAddr);
            case DOMAINNAME -> {
                outputStream.write(dstAddr.length);
                outputStream.write(dstAddr);
            }
        }
        outputStream.write(dstPort);
    }
}
