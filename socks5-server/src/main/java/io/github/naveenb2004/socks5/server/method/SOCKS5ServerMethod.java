package io.github.naveenb2004.socks5.server.method;

import io.github.naveenb2004.socks5.base.exception.SOCKS5ServiceException;

import java.io.InputStream;
import java.io.OutputStream;

public interface SOCKS5ServerMethod {
    byte getMethodId();

    void negotiateAsServer(InputStream inputStream,
                           OutputStream outputStream) throws SOCKS5ServiceException;

    InputStream setupDecapsulationAsServer(InputStream inputStream) throws SOCKS5ServiceException;

    OutputStream setupEncapsulationAsServer(OutputStream outputStream) throws SOCKS5ServiceException;
}
