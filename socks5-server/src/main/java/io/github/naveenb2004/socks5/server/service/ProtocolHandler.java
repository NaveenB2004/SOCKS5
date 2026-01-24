package io.github.naveenb2004.socks5.server.service;

import io.github.naveenb2004.socks5.server.SOCKS5ServerConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Socket;

public final class ProtocolHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProtocolHandler.class);

    private final Socket socket;
    private final SOCKS5ServerConfiguration configuration;

    public ProtocolHandler(Socket socket,
                           SOCKS5ServerConfiguration configuration) {
        this.socket = socket;
        this.configuration = configuration;
    }

    public boolean handle() {
        return false;
    }
}
