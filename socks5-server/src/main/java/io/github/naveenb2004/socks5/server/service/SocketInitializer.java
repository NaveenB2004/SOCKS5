package io.github.naveenb2004.socks5.server.service;

import io.github.naveenb2004.socks5.server.config.SOCKS5ServerConfiguration;
import io.github.naveenb2004.socks5.server.SOCKS5ServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public final class SocketInitializer {
    private static final Logger logger = LoggerFactory.getLogger(SocketInitializer.class);

    private final SOCKS5ServerConfiguration configuration;

    private Thread serverThread;
    private ServerSocket serverSocket;
    private ExecutorService executorService;
    private boolean initialized;

    public SocketInitializer(SOCKS5ServerConfiguration configuration) {
        this.configuration = configuration;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public synchronized void init() throws SOCKS5ServerException {
        if (initialized) throw new SOCKS5ServerException("Already initialized");
        serverSocket = createServerSocket(configuration);
        executorService = Executors.newFixedThreadPool(configuration.getMaximumClients(), configuration.getThreadFactory());
        serverThread = configuration.getThreadFactory().newThread(() -> {
            while (!serverSocket.isClosed()) {
                try {
                    Socket socket = serverSocket.accept();
                    executorService.execute(new ProtocolHandler(socket, configuration));
                } catch (IOException e) {
                    throw new SOCKS5ServerException(e);
                }
            }
        });
        serverThread.start();
        initialized = true;
    }

    public synchronized void destroy(long gracefulPeriod) throws SOCKS5ServerException {
        if (!initialized) throw new SOCKS5ServerException("Not initialized");
        try {
            serverSocket.close();
            executorService.shutdown();
            if (gracefulPeriod != 0 && !executorService.awaitTermination(gracefulPeriod, TimeUnit.MILLISECONDS)) {
                executorService.shutdownNow();
            }
            serverThread.interrupt();
            serverThread = null;
            serverSocket = null;
            executorService = null;
            initialized = false;
        } catch (IOException | InterruptedException e) {
            throw new SOCKS5ServerException(e);
        }
    }

    private static ServerSocket createServerSocket(SOCKS5ServerConfiguration configuration) throws SOCKS5ServerException {
        if (configuration.isSslEnabled()) {
            try {
                SSLContext sslContext = configuration.getSslContextProtocolProvider() == null ?
                        SSLContext.getInstance(configuration.getSslContextProtocol()) :
                        SSLContext.getInstance(configuration.getSslContextProtocol(), configuration.getSslContextProtocolProvider());
                sslContext.init(configuration.getKeyManagers(), configuration.getTrustManagers(), configuration.getSecureRandom());
                SSLServerSocketFactory sslServerSocketFactory = sslContext.getServerSocketFactory();
                try (SSLServerSocket serverSocket = (SSLServerSocket) sslServerSocketFactory.createServerSocket(
                        configuration.getSocketPort(),
                        configuration.getSocketBacklog(),
                        configuration.getSocketBindAddress())) {
                    serverSocket.setSSLParameters(configuration.getSslParameters());
                    return serverSocket;
                }
            } catch (NoSuchAlgorithmException | KeyManagementException | NoSuchProviderException | IOException e) {
                throw new SOCKS5ServerException(e);
            }
        } else {
            try (ServerSocket serverSocket = new ServerSocket(
                    configuration.getSocketPort(),
                    configuration.getSocketBacklog(),
                    configuration.getSocketBindAddress())) {
                return serverSocket;
            } catch (IOException e) {
                throw new SOCKS5ServerException(e);
            }
        }
    }
}
