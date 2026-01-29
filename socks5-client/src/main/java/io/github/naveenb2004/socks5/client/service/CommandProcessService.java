package io.github.naveenb2004.socks5.client.service;

import java.net.Socket;

public final class CommandProcessService {
    private final Socket socket;

    public CommandProcessService(final Socket socket) {
        this.socket = socket;
    }
}
