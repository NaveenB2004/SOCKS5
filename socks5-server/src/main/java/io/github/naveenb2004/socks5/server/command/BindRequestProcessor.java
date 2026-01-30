package io.github.naveenb2004.socks5.server.command;

import io.github.naveenb2004.socks5.base.command.CMD;
import io.github.naveenb2004.socks5.server.config.SOCKS5Ruleset;

import java.net.Socket;

public final class BindRequestProcessor extends SOCKS5RequestProcessor {
    public BindRequestProcessor(Socket clientSocket,
                                SOCKS5Ruleset socks5Ruleset) {
        super(CMD.BIND, clientSocket, socks5Ruleset);
    }
}
