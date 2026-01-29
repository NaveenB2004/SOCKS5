package io.github.naveenb2004.socks5.server.method;

import io.github.naveenb2004.socks5.server.exception.SOCKS5ServerException;

import java.io.InputStream;
import java.io.OutputStream;

public interface SOCKS5ServerMethod {
    byte getMethodId();

    void negotiateAsServer(InputStream inputStream,
                           OutputStream outputStream) throws SOCKS5ServerException;

    InputStream setupDecapsulationAsServer(InputStream inputStream) throws SOCKS5ServerException;

    OutputStream setupEncapsulationAsServer(OutputStream outputStream) throws SOCKS5ServerException;
}
