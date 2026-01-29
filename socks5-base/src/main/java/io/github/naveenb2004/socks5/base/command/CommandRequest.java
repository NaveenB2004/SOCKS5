package io.github.naveenb2004.socks5.base.command;

import io.github.naveenb2004.socks5.base.ATYP;
import io.github.naveenb2004.socks5.base.ImmutableObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;

@ImmutableObject
public record CommandRequest(CMD cmd,
                             ATYP atyp,
                             InetSocketAddress dest) {
}
