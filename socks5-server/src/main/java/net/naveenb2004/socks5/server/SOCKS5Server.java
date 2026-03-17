/*
 * Copyright (c) 2026 Naveen N. Balasooriya
 *
 * This source code is licensed under the MIT License.
 * See the LICENSE file in the project root for full license information.
 */

package net.naveenb2004.socks5.server;

import net.naveenb2004.socks5.server.configuration.SOCKS5ServerConfig;
import net.naveenb2004.socks5.server.exception.SOCKS5ServerException;
import net.naveenb2004.socks5.server.service.ClientService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class SOCKS5Server {
    private final SOCKS5ServerConfig config;

    private Thread serverThread;
    private ServerSocket serverSocket;
    private ExecutorService clientExecutor;
    private boolean initialized = false;

    private SOCKS5Server(final SOCKS5ServerConfig config) {
        this.config = config;
    }

    public synchronized void start() throws IOException {
        if (initialized) throw new SOCKS5ServerException("Server already started");
        this.clientExecutor = Executors.newFixedThreadPool(config.getConcurrentConnections(), config.getConcurrentThreadFactory());

        ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(config.getServerBindPoint(), config.getServerBacklog());
        serverSocket.setReceiveBufferSize(config.getInternalBufferSize());
        serverSocket.setSoTimeout(config.getConnectionTimeout());
        this.serverSocket = serverSocket;
        this.serverThread = Thread.ofPlatform().start(() -> {
            while (!serverSocket.isClosed()) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    clientSocket.setTcpNoDelay(true);
                    ClientService clientService = new ClientService(clientSocket, config);
                    clientExecutor.execute(clientService);
                } catch (IOException e) {
                    throw new SOCKS5ServerException(e);
                }
            }
        });

        initialized = true;
    }

    public synchronized void stop() throws IOException {
        if (!initialized) throw new SOCKS5ServerException("Server already stopped");
        if (!serverSocket.isClosed()) serverSocket.close();
        if (!clientExecutor.isShutdown()) clientExecutor.shutdownNow();
        if (serverThread.isAlive()) serverThread.interrupt();
    }

    public static SOCKS5ServerBuilder builder() {
        return new SOCKS5ServerBuilder();
    }

    public static final class SOCKS5ServerBuilder {
        private SOCKS5ServerConfig config;

        private SOCKS5ServerBuilder() {
        }

        public SOCKS5ServerBuilder config(final SOCKS5ServerConfig config) {
            this.config = config;
            return this;
        }

        public SOCKS5Server build() {
            if (config == null) throw new SOCKS5ServerException("No configuration provided");
            return new SOCKS5Server(config);
        }
    }
}
