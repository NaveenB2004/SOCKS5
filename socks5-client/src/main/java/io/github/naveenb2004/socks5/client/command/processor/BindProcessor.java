package io.github.naveenb2004.socks5.client.command.processor;

import io.github.naveenb2004.socks5.base.ATYP;
import io.github.naveenb2004.socks5.base.CMD;
import io.github.naveenb2004.socks5.client.command.BindResponse;
import io.github.naveenb2004.socks5.client.command.SOCKS5Response;

import java.io.IOException;
import java.net.Socket;

public final class BindProcessor extends CommandProcessor {
    private final BindResponse response;

    public BindProcessor(Socket socks5Server,
                         ATYP atyp,
                         byte[] dstAddr,
                         byte[] dstPort) throws IOException {
        var response = new BindResponse(socks5Server);
        super(socks5Server, CMD.BIND, atyp, dstAddr, dstPort, response);
        this.response = response;
    }

    @Override
    public SOCKS5Response process() {
        return response;
    }
}
