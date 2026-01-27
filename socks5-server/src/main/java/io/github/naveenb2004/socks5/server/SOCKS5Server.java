package io.github.naveenb2004.socks5.server;

import io.github.naveenb2004.socks5.server.exception.SOCKS5ServerException;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

public final class SOCKS5Server {
    private final int port;
    private final int backlog;
    private final InetAddress bindAddress;

    private ServerSocket serverSocket;

    private SOCKS5Server(int port,
                         int backlog,
                         InetAddress bindAddress) {
        this.port = port;
        this.backlog = backlog;
        this.bindAddress = bindAddress;
    }

    public int getPort() {
        return port;
    }

    public int getBacklog() {
        return backlog;
    }

    public InetAddress getBindAddress() {
        return bindAddress;
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public void init() throws SOCKS5ServerException {
        if (serverSocket != null) throw new SOCKS5ServerException("Server already initialized");
        try {
            if (bindAddress == null) {
                serverSocket = new ServerSocket(port, backlog);
            } else {
                serverSocket = new ServerSocket(port, backlog, bindAddress);
            }
        } catch (IOException e) {
            throw new SOCKS5ServerException(e);
        }
    }

    public void bootstrap() {

    }

    public void destroy() throws SOCKS5ServerException {
        if (serverSocket == null) throw new SOCKS5ServerException("Server not initialized");
        try {
            serverSocket.close();
        } catch (IOException e) {
            throw new SOCKS5ServerException(e);
        }
    }

    public static SOCKS5ServerBuilder builder() {
        return new SOCKS5ServerBuilder();
    }

    public static final class SOCKS5ServerBuilder {
        private int port;
        private int backlog;
        private InetAddress bindAddress;

        private SOCKS5ServerBuilder() {
        }

        public SOCKS5ServerBuilder port(int port) {
            this.port = port;
            return this;
        }

        public SOCKS5ServerBuilder backlog(int backlog) {
            this.backlog = backlog;
            return this;
        }

        public SOCKS5ServerBuilder bindAddress(InetAddress bindAddress) {
            this.bindAddress = bindAddress;
            return this;
        }

        public SOCKS5Server build() {
            if (port < 0 || port > 65535) throw new IllegalArgumentException("Port must be between 0 and 65535");
            return new SOCKS5Server(port, backlog, bindAddress);
        }
    }
}
