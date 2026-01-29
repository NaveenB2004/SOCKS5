package io.github.naveenb2004.socks5.base.command;

import io.github.naveenb2004.socks5.base.ATYP;
import io.github.naveenb2004.socks5.base.ImmutableObject;

import java.net.InetAddress;
import java.net.InetSocketAddress;

@ImmutableObject
public record CommandResponse(REP rep,
                              ATYP atyp,
                              InetSocketAddress dest) {
}
