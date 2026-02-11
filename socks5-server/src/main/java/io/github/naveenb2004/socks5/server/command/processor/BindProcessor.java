package io.github.naveenb2004.socks5.server.command.processor;

import io.github.naveenb2004.socks5.base.ATYP;

import java.net.InetSocketAddress;
import java.net.Socket;

public final class BindProcessor extends CommandProcessor {
    public BindProcessor(Socket socks5Client,
                         ATYP dstAtyp,
                         InetSocketAddress dst) {
        super(socks5Client, dstAtyp, dst);
    }

    @Override
    public void process() {

    }
}
