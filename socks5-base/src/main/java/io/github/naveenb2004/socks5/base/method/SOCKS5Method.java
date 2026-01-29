package io.github.naveenb2004.socks5.base.method;

import io.github.naveenb2004.socks5.base.exception.SOCKS5ServiceException;

import java.io.InputStream;
import java.io.OutputStream;

public interface SOCKS5Method {
    byte getMethodId();

    void negotiate(InputStream inputStream,
                   OutputStream outputStream) throws SOCKS5ServiceException;

    InputStream setupDecapsulation(InputStream inputStream) throws SOCKS5ServiceException;

    OutputStream setupEncapsulation(OutputStream outputStream) throws SOCKS5ServiceException;
}
