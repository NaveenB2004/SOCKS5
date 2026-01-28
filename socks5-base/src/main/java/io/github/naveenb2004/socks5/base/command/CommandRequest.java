package io.github.naveenb2004.socks5.base.command;

import io.github.naveenb2004.socks5.base.ATYP;
import io.github.naveenb2004.socks5.base.Immutable;

import java.net.InetAddress;

@Immutable
public record CommandRequest(CMD cmd,
                             ATYP atyp,
                             InetAddress dstAddr,
                             int dstPort) {
}
