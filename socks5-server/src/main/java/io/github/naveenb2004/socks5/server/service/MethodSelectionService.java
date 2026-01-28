package io.github.naveenb2004.socks5.server.service;

import io.github.naveenb2004.socks5.base.method.SOCKS5Methods;

import java.net.Socket;

public final class MethodSelectionService implements Runnable {
    private final Socket socket;
    private final SOCKS5Methods socks5Methods;

    public MethodSelectionService(Socket socket,
                                  SOCKS5Methods socks5Methods) {
        this.socket = socket;
        this.socks5Methods = socks5Methods;
    }

    @Override
    public void run() {

    }
}
