package io.github.naveenb2004.socks5.base.command;

import io.github.naveenb2004.socks5.base.ATYP;

import java.net.InetAddress;

public record CommandRequest(CMD cmd,
                             ATYP atyp,
                             InetAddress dstAddr,
                             int dstPort) {
}
