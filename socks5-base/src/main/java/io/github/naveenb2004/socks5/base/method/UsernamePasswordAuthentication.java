package io.github.naveenb2004.socks5.base.method;

public final class UsernamePasswordAuthentication implements SOCKS5Method {
    @Override
    public byte getMethodId() {
        return 0x02;
    }
}
