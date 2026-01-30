package io.github.naveenb2004.socks5.client.command;

import io.github.naveenb2004.socks5.base.command.CMD;
import io.github.naveenb2004.socks5.client.command.response.UdpAssociateResponse;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

public final class UdpAssociateRequestProcessor extends SOCKS5RequestProcessor {
    public UdpAssociateRequestProcessor(Socket socks5Server,
                                        InetSocketAddress destination) {
        super(CMD.UDP_ASSOCIATE, socks5Server, destination);
    }

    @Override
    public UdpAssociateResponse execute() {
        return null;
    }
}
