package io.github.naveenb2004.socks5.client.command.processor;

import io.github.naveenb2004.socks5.base.ATYP;
import io.github.naveenb2004.socks5.base.CMD;
import io.github.naveenb2004.socks5.client.command.SOCKS5Response;

import java.net.Socket;

public final class BindProcessor extends CommandProcessor {
    public BindProcessor(Socket socks5Server,
                         ATYP atyp,
                         byte[] dstAddr,
                         byte[] dstPort) {
        super(socks5Server, CMD.BIND, atyp, dstAddr, dstPort);
    }

    @Override
    public SOCKS5Response process() {
        return null;
    }
}
