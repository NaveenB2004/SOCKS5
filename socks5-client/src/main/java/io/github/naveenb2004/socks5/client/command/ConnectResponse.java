package io.github.naveenb2004.socks5.client.command;

import io.github.naveenb2004.socks5.base.ATYP;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public final class ConnectResponse implements SOCKS5Response {
    private final Socket sock5Server;
    private ATYP addressType;
    private InetSocketAddress bindSocket;

    public ConnectResponse(Socket sock5Server) {
        this.sock5Server = sock5Server;
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

    public InputStream getInputStream() throws IOException {
        return sock5Server.getInputStream();
    }

    public OutputStream getOutputStream() throws IOException {
        return sock5Server.getOutputStream();
    }

    @Override
    public void close() throws IOException {
        sock5Server.close();
    }
}
