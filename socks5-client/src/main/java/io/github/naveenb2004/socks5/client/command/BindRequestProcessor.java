package io.github.naveenb2004.socks5.client.command;

import io.github.naveenb2004.socks5.base.command.CMD;
import io.github.naveenb2004.socks5.client.command.response.BindResponse;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

public final class BindRequestProcessor extends SOCKS5RequestProcessor {
    public BindRequestProcessor(Socket socks5Server,
                                InetSocketAddress destination) {
        super(CMD.BIND, socks5Server, destination);
    }

    @Override
    public BindResponse execute() {
        return null;
    }
}
