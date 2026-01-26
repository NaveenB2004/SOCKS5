package io.github.naveenb2004.socks5.server.auth;

import java.io.InputStream;
import java.io.OutputStream;

public interface SOCKS5ServerAuth {
    byte getMethodId();

    void authenticateAsServer(InputStream inputStream,
                              OutputStream outputStream);

    InputStream getDecapsulationServerInputStream(InputStream inputStream);

    OutputStream getEncapsulationServerOutputStream(OutputStream outputStream);
}
