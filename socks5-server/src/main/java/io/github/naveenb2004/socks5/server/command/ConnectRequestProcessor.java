package io.github.naveenb2004.socks5.server.command;

import io.github.naveenb2004.socks5.base.command.CMD;
import io.github.naveenb2004.socks5.server.config.SOCKS5Ruleset;

import java.net.Socket;

public final class ConnectRequestProcessor extends SOCKS5RequestProcessor {
    public ConnectRequestProcessor(Socket clientSocket,
                                   SOCKS5Ruleset socks5Ruleset) {
        super(CMD.CONNECT, clientSocket, socks5Ruleset);
    }
}
