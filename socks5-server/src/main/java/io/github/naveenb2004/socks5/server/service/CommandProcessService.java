package io.github.naveenb2004.socks5.server.service;

import io.github.naveenb2004.socks5.server.config.SOCKS5Ruleset;

import java.net.Socket;

public final class CommandProcessService {
    private final Socket socket;
    private final SOCKS5Ruleset socks5Ruleset;

    public CommandProcessService(Socket socket,
                                 SOCKS5Ruleset socks5Ruleset) {
        this.socket = socket;
        this.socks5Ruleset = socks5Ruleset;
    }

    public void init() {

    }
}
