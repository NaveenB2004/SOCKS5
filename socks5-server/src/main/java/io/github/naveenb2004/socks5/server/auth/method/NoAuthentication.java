package io.github.naveenb2004.socks5.server.auth.method;

import io.github.naveenb2004.socks5.server.auth.SOCKS5ServerAuth;

import java.io.InputStream;
import java.io.OutputStream;

public record NoAuthentication() implements SOCKS5ServerAuth {
    @Override
    public byte getMethodId() {
        return 0x00;
    }

    @Override
    public void authenticateAsServer(InputStream inputStream,
                                     OutputStream outputStream) {
    }

    @Override
    public InputStream getDecapsulationServerInputStream(InputStream inputStream) {
        return inputStream;
    }

    @Override
    public OutputStream getEncapsulationServerOutputStream(OutputStream outputStream) {
        return outputStream;
    }
}
