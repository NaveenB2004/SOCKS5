package io.github.naveenb2004.socks5.server;

import io.github.naveenb2004.socks5.server.config.SOCKS5ServerConfiguration;
import io.github.naveenb2004.socks5.server.exception.SOCKS5ServerException;
import io.github.naveenb2004.socks5.server.exception.SOCKS5ServerServiceException;
import io.github.naveenb2004.socks5.server.service.MethodSelectionService;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public final class SOCKS5Server {
    private final SOCKS5ServerConfiguration configuration;

    private ServerSocket serverSocket;
    private Thread serverThread;
    private ExecutorService clientExecutor;
    private boolean bootstrapped;

    private SOCKS5Server(SOCKS5ServerConfiguration configuration) {
        this.configuration = configuration;
    }

    public SOCKS5ServerConfiguration getConfiguration() {
        return configuration;
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public synchronized void init() throws SOCKS5ServerException, SOCKS5ServerServiceException {
        if (serverSocket != null) throw new SOCKS5ServerException("Server already initialized");
        try {
            if (configuration.getBindAddress() == null) {
                serverSocket = new ServerSocket(
                        configuration.getPort(),
                        configuration.getBacklog());
            } else {
                serverSocket = new ServerSocket(
                        configuration.getPort(),
                        configuration.getBacklog(),
                        configuration.getBindAddress());
            }
            clientExecutor = Executors.newFixedThreadPool(
                    configuration.getMaxClients(),
                    configuration.getClientThreadFactory());
            bootstrapped = false;
        } catch (IOException e) {
            throw new SOCKS5ServerServiceException(e);
        }
    }

    public synchronized void bootstrap() throws SOCKS5ServerException {
        if (serverSocket == null) throw new SOCKS5ServerException("Server not initialized");
        if (bootstrapped) throw new SOCKS5ServerException("Server already bootstrapped");
        serverThread = Thread.ofPlatform().factory().newThread(() -> {
            while (!serverSocket.isClosed()) {
                clientExecutor.execute(() -> {
                    try {
                        new MethodSelectionService(serverSocket.accept(), configuration.getSocks5Methods()).init();
                    } catch (IOException | SOCKS5ServerServiceException e) {
                        throw new SOCKS5ServerException(e);
                    }
                });
            }
        });
        serverThread.start();
        bootstrapped = true;
    }

    public synchronized void destroy() throws SOCKS5ServerException, SOCKS5ServerServiceException {
        if (serverSocket == null) throw new SOCKS5ServerException("Server not initialized");
        try {
            serverSocket.close();
            if (serverThread.isAlive()) serverThread.interrupt();
            clientExecutor.shutdown();
            if (!clientExecutor.awaitTermination(10, TimeUnit.SECONDS)) {
                clientExecutor.shutdownNow();
            }
            serverSocket = null;
            bootstrapped = false;
        } catch (IOException | InterruptedException e) {
            throw new SOCKS5ServerServiceException(e);
        }
    }

    public static SOCKS5ServerBuilder builder() {
        return new SOCKS5ServerBuilder();
    }

    public static final class SOCKS5ServerBuilder {
        private SOCKS5ServerConfiguration configuration;

        private SOCKS5ServerBuilder() {
        }

        public SOCKS5ServerBuilder configuration(SOCKS5ServerConfiguration configuration) {
            this.configuration = configuration;
            return this;
        }

        public SOCKS5Server build() throws SOCKS5ServerException {
            if (configuration == null) throw new SOCKS5ServerException("Configuration not set");
            return new SOCKS5Server(configuration);
        }
    }
}
