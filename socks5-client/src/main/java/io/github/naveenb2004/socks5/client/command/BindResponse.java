package io.github.naveenb2004.socks5.client.command;

import io.github.naveenb2004.socks5.base.ATYP;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public final class BindResponse implements SOCKS5Response {
    private final Socket socks5Server;
    private ATYP addressType;
    private InetSocketAddress bindSocket;

    public BindResponse(Socket socks5Server) {
        this.socks5Server = socks5Server;
    }

    @Override
    public void setAddressType(ATYP addressType) {
        this.addressType = addressType;
    }

    @Override
    public ATYP getAddressType() {
        return addressType;
    }

    @Override
    public void setBindSocket(InetSocketAddress bindSocket) {
        this.bindSocket = bindSocket;
    }

    @Override
    public InetSocketAddress getBindSocket() {
        return bindSocket;
    }

    @Override
    public void close() throws IOException {
        socks5Server.close();
    }
}
