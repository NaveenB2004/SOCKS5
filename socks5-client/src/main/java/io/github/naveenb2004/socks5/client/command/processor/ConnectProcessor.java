package io.github.naveenb2004.socks5.client.command.processor;

import io.github.naveenb2004.socks5.client.command.SOCKS5Response;

import java.net.InetSocketAddress;
import java.net.Socket;

public final class ConnectProcessor implements CommandProcessor {
    private final Socket socks5Server;
    private final InetSocketAddress destination;

    public ConnectProcessor(Socket socks5Server,
                            InetSocketAddress destination) {
        this.socks5Server = socks5Server;
        this.destination = destination;
    }

    @Override
    public SOCKS5Response process() {
        return null;
    }
}
