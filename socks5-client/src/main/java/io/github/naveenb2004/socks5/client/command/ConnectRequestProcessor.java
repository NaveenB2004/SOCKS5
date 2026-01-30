package io.github.naveenb2004.socks5.client.command;

import io.github.naveenb2004.socks5.base.command.CMD;
import io.github.naveenb2004.socks5.client.command.response.ConnectResponse;

import java.net.InetSocketAddress;
import java.net.Socket;

public final class ConnectRequestProcessor extends SOCKS5RequestProcessor {

    public ConnectRequestProcessor(Socket socks5Server,
                                   InetSocketAddress destination) {
        super(CMD.CONNECT, socks5Server, destination);
    }

    @Override
    public ConnectResponse execute() {
        return null;
    }
}
