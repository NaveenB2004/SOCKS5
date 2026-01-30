package io.github.naveenb2004.socks5.client;

import io.github.naveenb2004.socks5.base.command.CMD;
import io.github.naveenb2004.socks5.client.command.response.SOCKS5Response;
import io.github.naveenb2004.socks5.client.config.SOCKS5ClientConfiguration;
import io.github.naveenb2004.socks5.client.exception.SOCKS5ClientException;
import io.github.naveenb2004.socks5.client.service.CommandProcessService;
import io.github.naveenb2004.socks5.client.service.MethodSelectionService;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public final class SOCKS5Client {
    private final SOCKS5ClientConfiguration configuration;

    private Socket socket;
    private boolean bootstrapped;

    private SOCKS5Client(SOCKS5ClientConfiguration configuration) {
        this.configuration = configuration;
    }

    public SOCKS5ClientConfiguration getConfiguration() {
        return configuration;
    }

    public Socket getSocket() {
        return socket;
    }

    public synchronized void init() throws SOCKS5ClientException {
        if (socket != null) throw new SOCKS5ClientException("Socket already initialized");
        try {
            if (configuration.getLocalAddress() == null) {
                socket = new Socket(configuration.getServerAddress(), configuration.getServerPort());
            } else {
                socket = new Socket(configuration.getLocalAddress(), configuration.getServerPort(),
                        configuration.getLocalAddress(), configuration.getLocalPort());
            }
        } catch (IOException e) {
            throw new SOCKS5ClientException(e);
        }
    }

    public synchronized SOCKS5Response bootstrap(CMD command,
                                                 InetSocketAddress destination) throws SOCKS5ClientException {
        if (bootstrapped) throw new SOCKS5ClientException("Already bootstrapped");
        if (socket == null) throw new SOCKS5ClientException("Socket not initialized");
        if (command == null) throw new SOCKS5ClientException("Command cannot be null");
        if (destination == null) throw new SOCKS5ClientException("Destination cannot be null");
        try {
            new MethodSelectionService(socket, configuration.getSOCKS5Methods()).init();
            SOCKS5Response response = new CommandProcessService(command, socket, destination).init();
            bootstrapped = true;
            return response;
        } catch (SOCKS5ClientException e) {
            destroy();
            throw e;
        }
    }

    public synchronized void destroy() throws SOCKS5ClientException {
        if (socket == null) throw new SOCKS5ClientException("Socket not initialized");
        try {
            if (!socket.isClosed()) socket.close();
            socket = null;
            bootstrapped = false;
        } catch (IOException e) {
            throw new SOCKS5ClientException(e);
        }
    }

    public static SOCKS5ClientBuilder builder() {
        return new SOCKS5ClientBuilder();
    }

    public static final class SOCKS5ClientBuilder {
        private SOCKS5ClientConfiguration configuration;

        private SOCKS5ClientBuilder() {
        }

        public SOCKS5ClientBuilder configuration(SOCKS5ClientConfiguration configuration) {
            this.configuration = configuration;
            return this;
        }

        public SOCKS5Client build() {
            if (configuration == null) throw new SOCKS5ClientException("Configuration not set");
            return new SOCKS5Client(configuration);
        }
    }
}
