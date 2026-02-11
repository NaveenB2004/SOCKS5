package io.github.naveenb2004.socks5.client.command.processor;

import io.github.naveenb2004.socks5.base.ATYP;
import io.github.naveenb2004.socks5.base.CMD;
import io.github.naveenb2004.socks5.client.command.SOCKS5Response;
import io.github.naveenb2004.socks5.client.command.UdpAssociateResponse;

import java.io.IOException;
import java.net.Socket;

public final class UdpAssociateProcessor extends CommandProcessor {
    private final UdpAssociateResponse response;

    public UdpAssociateProcessor(Socket socks5Server,
                                 ATYP atyp,
                                 byte[] dstAddr,
                                 byte[] dstPort) throws IOException {
        var response = new UdpAssociateResponse(socks5Server);
        super(socks5Server, CMD.UDP_ASSOCIATE, atyp, dstAddr, dstPort, response);
        this.response = response;
    }

    @Override
    public SOCKS5Response process() {
        return response;
    }
}
