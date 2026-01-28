package io.github.naveenb2004.socks5.client.service;

import io.github.naveenb2004.socks5.base.method.SOCKS5Methods;

import java.net.Socket;

public final class MethodSelectionService {
    private final Socket socket;
    private final SOCKS5Methods socks5Methods;

    public MethodSelectionService(Socket socket,
                                  SOCKS5Methods socks5Methods) {
        this.socket = socket;
        this.socks5Methods = socks5Methods;
    }

    public void run() {

    }
}
