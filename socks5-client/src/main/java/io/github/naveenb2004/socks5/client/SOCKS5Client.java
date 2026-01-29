package io.github.naveenb2004.socks5.client;

import io.github.naveenb2004.socks5.client.command.response.BindResponse;
import io.github.naveenb2004.socks5.client.command.response.ConnectResponse;
import io.github.naveenb2004.socks5.client.command.response.UdpAssociateResponse;
import io.github.naveenb2004.socks5.client.exception.SOCKS5ClientException;
import io.github.naveenb2004.socks5.client.exception.SOCKS5ClientServiceException;
import io.github.naveenb2004.socks5.client.service.MethodSelectionService;

import java.io.IOException;
import java.net.Socket;

public final class SOCKS5Client {
    private final SOCKS5ClientConfiguration configuration;

    private Socket socket;

    private SOCKS5Client(SOCKS5ClientConfiguration configuration) {
        this.configuration = configuration;
    }

    public SOCKS5ClientConfiguration getConfiguration() {
        return configuration;
    }

    public Socket getSocket() {
        return socket;
    }

    public synchronized void init() throws SOCKS5ClientException, SOCKS5ClientServiceException {
        if (socket != null) throw new SOCKS5ClientException("Socket already initialized");
        try {
            if (configuration.getLocalAddress() == null) {
                socket = new Socket(configuration.getServerAddress(), configuration.getServerPort());
            } else {
                socket = new Socket(configuration.getLocalAddress(), configuration.getServerPort(),
                        configuration.getLocalAddress(), configuration.getLocalPort());
            }
        } catch (IOException e) {
            throw new SOCKS5ClientServiceException(e);
        }
    }

    private void doMethodSelectionAndNegotiation() throws SOCKS5ClientServiceException {
        try {
            new MethodSelectionService(socket, configuration.getSOCKS5Methods()).init();
        } catch (SOCKS5ClientServiceException e) {
            destroy();
            throw e;
        }
    }

    public BindResponse bind() throws SOCKS5ClientException, SOCKS5ClientServiceException {
        if (socket == null) throw new SOCKS5ClientException("Socket not initialized");
        doMethodSelectionAndNegotiation();
        return null;
    }

    public ConnectResponse connect() throws SOCKS5ClientException, SOCKS5ClientServiceException {
        if (socket == null) throw new SOCKS5ClientException("Socket not initialized");
        doMethodSelectionAndNegotiation();
        return null;
    }

    public UdpAssociateResponse udpAssociate() throws SOCKS5ClientException, SOCKS5ClientServiceException {
        if (socket == null) throw new SOCKS5ClientException("Socket not initialized");
        doMethodSelectionAndNegotiation();
        return null;
    }

    public synchronized void destroy() throws SOCKS5ClientException, SOCKS5ClientServiceException {
        if (socket == null) throw new SOCKS5ClientException("Socket not initialized");
        try {
            if (!socket.isClosed()) socket.close();
            socket = null;
        } catch (IOException e) {
            throw new SOCKS5ClientServiceException(e);
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
