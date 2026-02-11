package io.github.naveenb2004.socks5.client.command;

import io.github.naveenb2004.socks5.base.ATYP;

import java.io.IOException;
import java.net.InetSocketAddress;

public interface SOCKS5Response {
    void setAddressType(ATYP addressType);

    ATYP getAddressType();

    void setBindSocket(InetSocketAddress bindSocket);

    InetSocketAddress getBindSocket();

    void close() throws IOException;
}
