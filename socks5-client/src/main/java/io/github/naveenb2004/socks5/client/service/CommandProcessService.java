package io.github.naveenb2004.socks5.client.service;

import io.github.naveenb2004.socks5.base.command.CMD;
import io.github.naveenb2004.socks5.client.command.BindRequestProcessor;
import io.github.naveenb2004.socks5.client.command.ConnectRequestProcessor;
import io.github.naveenb2004.socks5.client.command.SOCKS5RequestProcessor;
import io.github.naveenb2004.socks5.client.command.UdpAssociateRequestProcessor;
import io.github.naveenb2004.socks5.client.command.response.SOCKS5Response;

import java.net.InetSocketAddress;
import java.net.Socket;

// TODO rethink!!!
public final class CommandProcessService {
    private final CMD command;
    private final Socket socks5Server;
    private final InetSocketAddress destination;

    public CommandProcessService(CMD command,
                                 Socket socks5Server,
                                 InetSocketAddress destination) {
        this.command = command;
        this.socks5Server = socks5Server;
        this.destination = destination;
    }

    public SOCKS5Response init() {
        SOCKS5RequestProcessor request = switch (command) {
            case CONNECT -> new ConnectRequestProcessor(socks5Server, destination);
            case BIND -> new BindRequestProcessor(socks5Server, destination);
            case UDP_ASSOCIATE -> new UdpAssociateRequestProcessor(socks5Server, destination);
        };
        return request.execute();
    }
}
