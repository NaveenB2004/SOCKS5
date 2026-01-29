package io.github.naveenb2004.socks5.server.config;

import io.github.naveenb2004.socks5.base.Immutable;
import io.github.naveenb2004.socks5.server.exception.SOCKS5ServerConfigException;
import io.github.naveenb2004.socks5.server.exception.SOCKS5ServerException;
import io.github.naveenb2004.socks5.server.method.NoAuthentication;
import io.github.naveenb2004.socks5.server.method.SOCKS5ServerMethod;

import java.net.InetAddress;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.SequencedSet;
import java.util.concurrent.ThreadFactory;

@Immutable
public final class SOCKS5ServerConfiguration {
    private final int port;
    private final int backlog;
    private final InetAddress bindAddress;
    private final int maxClients;
    private final ThreadFactory clientThreadFactory;
    private final List<SOCKS5ServerMethod> socks5ServerMethods;
    private final SOCKS5Ruleset socks5Ruleset;

    private SOCKS5ServerConfiguration(int port,
                                      int backlog,
                                      InetAddress bindAddress,
                                      int maxClients,
                                      ThreadFactory clientThreadFactory,
                                      List<SOCKS5ServerMethod> socks5ServerMethods,
                                      SOCKS5Ruleset socks5Ruleset) {
        this.port = port;
        this.backlog = backlog;
        this.bindAddress = bindAddress;
        this.maxClients = maxClients;
        this.clientThreadFactory = clientThreadFactory;
        this.socks5ServerMethods = socks5ServerMethods;
        this.socks5Ruleset = socks5Ruleset;
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

    public List<SOCKS5ServerMethod> getSocks5Methods() {
        return socks5ServerMethods;
    }

    public SOCKS5Ruleset getSocks5Ruleset() {
        return socks5Ruleset;
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
        private final SequencedSet<SOCKS5ServerMethod> socks5ServerMethods = new LinkedHashSet<>();
        private SOCKS5Ruleset socks5Ruleset;

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

        public SOCKS5ServerConfigurationBuilder addSocks5MethodImpl(SOCKS5ServerMethod socks5ServerMethod) throws SOCKS5ServerConfigException {
            if (socks5ServerMethod == null) throw new SOCKS5ServerException("SOCKS5 method cannot be null");
            this.socks5ServerMethods.add(socks5ServerMethod);
            return this;
        }

        public SOCKS5ServerConfigurationBuilder socks5Ruleset(SOCKS5Ruleset socks5Ruleset) {
            this.socks5Ruleset = socks5Ruleset;
            return this;
        }

        public SOCKS5ServerConfiguration build() {
            if (socks5ServerMethods.isEmpty()) socks5ServerMethods.add(NoAuthentication.builder().build());
            return new SOCKS5ServerConfiguration(port, backlog, bindAddress, maxClients,
                    clientThreadFactory, List.copyOf(socks5ServerMethods), socks5Ruleset);
        }
    }
}
