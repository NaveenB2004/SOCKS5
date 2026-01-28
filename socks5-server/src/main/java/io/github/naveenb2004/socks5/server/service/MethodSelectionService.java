package io.github.naveenb2004.socks5.server.service;

import io.github.naveenb2004.socks5.base.method.SOCKS5Method;

import java.net.Socket;
import java.util.List;

public final class MethodSelectionService implements Runnable {
    private final Socket socket;
    private final List<SOCKS5Method> socks5Methods;

    public MethodSelectionService(Socket socket,
                                  List<SOCKS5Method> socks5Methods) {
        this.socket = socket;
        this.socks5Methods = socks5Methods;
    }

    @Override
    public void run() {

    }
}
