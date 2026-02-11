package io.github.naveenb2004.socks5.client;

import io.github.naveenb2004.socks5.base.ATYP;
import io.github.naveenb2004.socks5.base.CMD;
import io.github.naveenb2004.socks5.client.command.SOCKS5Response;
import io.github.naveenb2004.socks5.client.command.processor.BindProcessor;
import io.github.naveenb2004.socks5.client.command.processor.CommandProcessor;
import io.github.naveenb2004.socks5.client.command.processor.ConnectProcessor;
import io.github.naveenb2004.socks5.client.command.processor.UdpAssociateProcessor;
import io.github.naveenb2004.socks5.client.config.SOCKS5ClientConfiguration;
import io.github.naveenb2004.socks5.client.exception.SOCKS5ClientException;
import io.github.naveenb2004.socks5.client.method.service.MethodSelectionService;

import java.io.IOException;
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

    private synchronized SOCKS5Response bootstrap(CMD cmd,
                                                  ATYP atyp,
                                                  byte[] dstAddr,
                                                  byte[] dstPort) throws SOCKS5ClientException {
        if (bootstrapped) throw new SOCKS5ClientException("Already bootstrapped");
        if (socket == null) throw new SOCKS5ClientException("Socket not initialized");
        if (cmd == null) throw new SOCKS5ClientException("Command cannot be null");
        if (atyp == null) throw new SOCKS5ClientException("Address type cannot be null");
        if (dstAddr == null) throw new SOCKS5ClientException("Destination address cannot be null");
        if (dstPort == null) throw new SOCKS5ClientException("Destination port cannot be null");
        try {
            new MethodSelectionService(socket, configuration.getSOCKS5Methods()).init();
            CommandProcessor commandProcessor = switch (cmd) {
                case CONNECT -> new ConnectProcessor(socket, atyp, dstAddr, dstPort);
                case BIND -> null;
                case UDP_ASSOCIATE -> null;
            };
            SOCKS5Response response = commandProcessor.process();
            bootstrapped = true;
            return response;
        } catch (Exception e) {
            destroy();
            throw new SOCKS5ClientException(e);
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
