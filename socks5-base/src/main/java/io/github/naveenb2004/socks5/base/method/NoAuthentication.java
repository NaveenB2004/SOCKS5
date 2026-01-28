package io.github.naveenb2004.socks5.base.method;

public final class NoAuthentication implements SOCKS5Method {
    @Override
    public byte getMethodId() {
        return 0x00;
    }
}
