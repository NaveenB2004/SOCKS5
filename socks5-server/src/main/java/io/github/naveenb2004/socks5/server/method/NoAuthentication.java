package io.github.naveenb2004.socks5.server.method;

import io.github.naveenb2004.socks5.base.ImmutableObject;

import java.io.InputStream;
import java.io.OutputStream;

@ImmutableObject
public final class NoAuthentication implements SOCKS5ServerMethod {
    private NoAuthentication() {
    }

    @Override
    public byte getMethodId() {
        return 0x00;
    }

    @Override
    public void negotiateAsServer(InputStream inputStream,
                                  OutputStream outputStream) {
    }

    @Override
    public InputStream setupDecapsulationAsServer(InputStream inputStream) {
        return inputStream;
    }

    @Override
    public OutputStream setupEncapsulationAsServer(OutputStream outputStream) {
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
