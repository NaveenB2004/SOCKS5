package io.github.naveenb2004.socks5.base.command;

import io.github.naveenb2004.socks5.base.ATYP;

import java.net.InetAddress;

public record CommandResponse(REP rep,
                              ATYP atyp,
                              InetAddress bndAddr,
                              int bndPort) {
}
