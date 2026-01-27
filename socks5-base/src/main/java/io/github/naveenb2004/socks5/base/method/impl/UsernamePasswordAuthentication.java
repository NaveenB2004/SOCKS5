package io.github.naveenb2004.socks5.base.method.impl;

import io.github.naveenb2004.socks5.base.method.SOCKS5MethodImpl;

public final class UsernamePasswordAuthentication implements SOCKS5MethodImpl {
    @Override
    public byte getMethodId() {
        return 0x02;
    }
}
