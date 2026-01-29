package io.github.naveenb2004.socks5.base.method;

import io.github.naveenb2004.socks5.base.exception.SOCKS5ServiceException;

import java.io.InputStream;
import java.io.OutputStream;

public final class NoAuthentication implements SOCKS5Method {
    private NoAuthentication() {
    }

    @Override
    public byte getMethodId() {
        return 0x00;
    }

    @Override
    public void negotiate(InputStream inputStream,
                          OutputStream outputStream) {
    }

    @Override
    public InputStream setupDecapsulation(InputStream inputStream) {
        return inputStream;
    }

    @Override
    public OutputStream setupEncapsulation(OutputStream outputStream) {
        return outputStream;
    }

    public static NoAuthenticationBuilder builder() {
        return new NoAuthenticationBuilder();
    }

    public static class NoAuthenticationBuilder {
        private NoAuthenticationBuilder() {
        }

        public NoAuthentication build() {
            return new NoAuthentication();
        }
    }
}
