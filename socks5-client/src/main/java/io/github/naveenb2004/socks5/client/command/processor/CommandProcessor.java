package io.github.naveenb2004.socks5.client.command.processor;

import io.github.naveenb2004.socks5.base.ATYP;
import io.github.naveenb2004.socks5.base.CMD;
import io.github.naveenb2004.socks5.base.REP;
import io.github.naveenb2004.socks5.client.command.SOCKS5Response;
import io.github.naveenb2004.socks5.client.exception.SOCKS5ClientException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;

public abstract sealed class CommandProcessor
        permits BindProcessor, ConnectProcessor, UdpAssociateProcessor {
    protected final Socket socks5Server;
    protected final InputStream inputStream;
    protected final OutputStream outputStream;

    protected final CMD cmd;
    protected final ATYP atyp;
    protected final byte[] dstAddr;
    protected final byte[] dstPort;

    protected final SOCKS5Response socks5Response;

    protected CommandProcessor(Socket socks5Server,
                               CMD cmd,
                               ATYP atyp,
                               byte[] dstAddr,
                               byte[] dstPort,
                               SOCKS5Response socks5Response) throws IOException {
        this.socks5Server = socks5Server;
        this.inputStream = socks5Server.getInputStream();
        this.outputStream = socks5Server.getOutputStream();

        this.cmd = cmd;
        this.atyp = atyp;
        this.dstAddr = dstAddr;
        this.dstPort = dstPort;
        this.socks5Response = socks5Response;
    }

    public abstract SOCKS5Response process();

    protected final void sendRequest() throws IOException {
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

    protected final void consumeResponse() throws IOException {
        int version = inputStream.read();
        if (version != 0x05) throw new SOCKS5ClientException("Invalid version from server");
        REP response = REP.valueOf(inputStream.read());
        if (response != REP.SUCCEEDED) throw new SOCKS5ClientException("SOCKS5 failed: " + response);
        int rsv = inputStream.read();
        if (rsv != 0x00) throw new SOCKS5ClientException("Invalid RSV");


        ATYP addressType = ATYP.valueOf(inputStream.read());
        byte[] bndAddr = switch (addressType) {
            case IP_V4_ADDRESS -> new byte[4];
            case IP_V6_ADDRESS -> new byte[16];
            case DOMAINNAME -> {
                int len = inputStream.read();
                yield new byte[len];
            }
        };
        int len = inputStream.read(bndAddr);
        if (len != bndAddr.length) throw new SOCKS5ClientException("Error while reading bind address");

        int bndPort = (inputStream.read() & 0xFF) << 8 | (inputStream.read() & 0xFF);

        socks5Response.setAddressType(addressType);
        socks5Response.setBindSocket(switch (addressType){
            case DOMAINNAME -> new InetSocketAddress(new String(bndAddr), bndPort);
            case IP_V4_ADDRESS, IP_V6_ADDRESS -> new InetSocketAddress(InetAddress.getByAddress(bndAddr), bndPort);
        });
    }
}
