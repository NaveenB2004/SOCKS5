package io.github.naveenb2004.socks5.client.method;

import io.github.naveenb2004.socks5.client.exception.SOCKS5ClientException;

import java.io.InputStream;
import java.io.OutputStream;

public interface SOCKS5ClientMethod {
    byte getMethodId();

    void negotiateAsClient(InputStream inputStream,
                           OutputStream outputStream) throws SOCKS5ClientException;

    InputStream setupDecapsulationAsClient(InputStream inputStream) throws SOCKS5ClientException;

    OutputStream setupEncapsulationAsClient(OutputStream outputStream) throws SOCKS5ClientException;
}
