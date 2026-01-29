package io.github.naveenb2004.socks5.server;

import io.github.naveenb2004.socks5.base.Immutable;
import io.github.naveenb2004.socks5.base.method.NoAuthentication;
import io.github.naveenb2004.socks5.base.method.SOCKS5Method;
import io.github.naveenb2004.socks5.server.exception.SOCKS5ServerConfigException;
import io.github.naveenb2004.socks5.server.exception.SOCKS5ServerException;

import java.net.InetAddress;
import java.util.*;
import java.util.concurrent.ThreadFactory;

@Immutable
public final class SOCKS5ServerConfiguration {
    private final int port;
    private final int backlog;
    private final InetAddress bindAddress;
    private final int maxClients;
    private final ThreadFactory clientThreadFactory;
    private final List<SOCKS5Method> socks5Methods;

    private SOCKS5ServerConfiguration(int port,
                                      int backlog,
                                      InetAddress bindAddress,
                                      int maxClients,
                                      ThreadFactory clientThreadFactory,
                                      List<SOCKS5Method> socks5Methods) {
        this.port = port;
        this.backlog = backlog;
        this.bindAddress = bindAddress;
        this.maxClients = maxClients;
        this.clientThreadFactory = clientThreadFactory;
        this.socks5Methods = socks5Methods;
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

    public int getMaxClients() {
        return maxClients;
    }

    public ThreadFactory getClientThreadFactory() {
        return clientThreadFactory;
    }

    public List<SOCKS5Method> getSocks5Methods() {
        return Collections.unmodifiableList(socks5Methods);
    }

    public static SOCKS5ServerConfigurationBuilder builder() {
        return new SOCKS5ServerConfigurationBuilder();
    }

    public static final class SOCKS5ServerConfigurationBuilder {
        private int port;
        private int backlog;
        private InetAddress bindAddress;
        private int maxClients = 100;
        private ThreadFactory clientThreadFactory = Thread.ofVirtual().factory();
        private SequencedSet<SOCKS5Method> socks5Methods = new LinkedHashSet<>();

        private SOCKS5ServerConfigurationBuilder() {
        }

        public SOCKS5ServerConfigurationBuilder port(int port) throws SOCKS5ServerConfigException {
            if (port < 0 || port > 65535) throw new SOCKS5ServerException("Port must be between 0 and 65535");
            this.port = port;
            return this;
        }

        public SOCKS5ServerConfigurationBuilder backlog(int backlog) {
            this.backlog = backlog;
            return this;
        }

        public SOCKS5ServerConfigurationBuilder bindAddress(InetAddress bindAddress) {
            this.bindAddress = bindAddress;
            return this;
        }

        public SOCKS5ServerConfigurationBuilder maxClients(int maxClients) throws SOCKS5ServerConfigException {
            if (maxClients < 1) throw new SOCKS5ServerException("Max clients must be greater than 0");
            this.maxClients = maxClients;
            return this;
        }

        public SOCKS5ServerConfigurationBuilder threadFactory(ThreadFactory clientThreadFactory) throws SOCKS5ServerConfigException {
            if (clientThreadFactory == null) throw new SOCKS5ServerException("Client thread factory cannot be null");
            this.clientThreadFactory = clientThreadFactory;
            return this;
        }

        public SOCKS5ServerConfigurationBuilder addSocks5MethodImpl(SOCKS5Method socks5Method) throws SOCKS5ServerConfigException {
            if (socks5Method == null) throw new SOCKS5ServerException("SOCKS5 method cannot be null");
            this.socks5Methods.add(socks5Method);
            return this;
        }

        public SOCKS5ServerConfiguration build() {
            if (socks5Methods.isEmpty()) socks5Methods.add(NoAuthentication.builder().build());
            List<SOCKS5Method> socks5MethodsList = new ArrayList<>(socks5Methods);
            return new SOCKS5ServerConfiguration(port, backlog, bindAddress, maxClients, clientThreadFactory, socks5MethodsList);
        }
    }
}
