/*
 * Copyright (c) 2026 Naveen N. Balasooriya
 *
 * This source code is licensed under the MIT License.
 * See the LICENSE file in the project root for full license information.
 */

package io.github.naveenb2004.socks5.server.configuration;

import io.github.naveenb2004.socks5.base.configuration.AbstractSOCKS5Configuration;
import io.github.naveenb2004.socks5.server.authentication.AbstractServerAuth;
import io.github.naveenb2004.socks5.server.exception.SOCKS5ServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadFactory;

public final class SOCKS5ServerConfig extends AbstractSOCKS5Configuration {
    private final int concurrentConnections;
    private final ThreadFactory concurrentThreadFactory;
    private final InetSocketAddress serverBindPoint;
    private final int serverBacklog;
    private final Map<Integer, AbstractServerAuth> serverAuths;
    private final SOCKS5ServerRuleset serverRuleset;

    private SOCKS5ServerConfig(final int internalBufferSize,
                               final int connectionTimeout,
                               final int concurrentConnections,
                               final ThreadFactory concurrentThreadFactory,
                               final InetSocketAddress serverBindPoint,
                               final int serverBacklog,
                               final Map<Integer, AbstractServerAuth> serverAuths,
                               final SOCKS5ServerRuleset serverRuleset) {
        super(internalBufferSize, connectionTimeout);
        this.concurrentConnections = concurrentConnections;
        this.concurrentThreadFactory = concurrentThreadFactory;
        this.serverBindPoint = serverBindPoint;
        this.serverBacklog = serverBacklog;
        this.serverAuths = serverAuths;
        this.serverRuleset = serverRuleset;
    }

    public int getConcurrentConnections() {
        return concurrentConnections;
    }

    public ThreadFactory getConcurrentThreadFactory() {
        return concurrentThreadFactory;
    }

    public InetSocketAddress getServerBindPoint() {
        return serverBindPoint;
    }

    public int getServerBacklog() {
        return serverBacklog;
    }

    public Map<Integer, AbstractServerAuth> getServerAuths() {
        return Collections.unmodifiableMap(serverAuths);
    }

    public SOCKS5ServerRuleset getServerRuleset() {
        return serverRuleset;
    }

    public static SOCKS5ServerConfigurationBuilder builder() {
        return new SOCKS5ServerConfigurationBuilder();
    }

    public static final class SOCKS5ServerConfigurationBuilder extends SOCKS5ConfigurationBuilder {
        private static final Logger LOGGER = LoggerFactory.getLogger(SOCKS5ServerConfigurationBuilder.class);

        private int concurrentConnections = 100;
        private ThreadFactory concurrentThreadFactory = Thread.ofVirtual().factory();
        private InetSocketAddress serverBindPoint = new InetSocketAddress("0.0.0.0", 1080);
        private int serverBacklog = 10;
        private final Map<Integer, AbstractServerAuth> serverAuths = new HashMap<>(1, 1);
        private SOCKS5ServerRuleset serverRuleset = null;

        private SOCKS5ServerConfigurationBuilder() {
            super();
        }

        public SOCKS5ServerConfigurationBuilder concurrentConnections(final int concurrentConnections) {
            if (concurrentConnections <= 0) throw new SOCKS5ServerException("Config error: concurrentConnections must be greater than 0.");
            this.concurrentConnections = concurrentConnections;
            return this;
        }

        public SOCKS5ServerConfigurationBuilder concurrentThreadFactory(final ThreadFactory concurrentThreadFactory) {
            if (concurrentThreadFactory == null) throw new SOCKS5ServerException("Config error: concurrentThreadFactory cannot be null.");
            this.concurrentThreadFactory = concurrentThreadFactory;
            return this;
        }

        public SOCKS5ServerConfigurationBuilder serverBindPoint(final InetSocketAddress serverBindPoint) {
            if (serverBindPoint == null) throw new SOCKS5ServerException("Config error: serverBindPoint cannot be null.");
            this.serverBindPoint = serverBindPoint;
            return this;
        }

        public SOCKS5ServerConfigurationBuilder serverBacklog(final int serverBacklog) {
            if (serverBacklog <= 0) LOGGER.atWarn().log("Config warning: serverBacklog <= 0; implementation specific default will be used.");
            this.serverBacklog = serverBacklog;
            return this;
        }

        public SOCKS5ServerConfigurationBuilder serverAuth(final AbstractServerAuth serverAuth) {
            if (serverAuth == null) throw new SOCKS5ServerException("Config error: serverAuth cannot be null.");
            this.serverAuths.put(serverAuth.getAuthMethodId(), serverAuth);
            return this;
        }

        public SOCKS5ServerConfigurationBuilder serverRuleset(final SOCKS5ServerRuleset serverRuleset) {
            this.serverRuleset = serverRuleset;
            return this;
        }

        @Override
        public SOCKS5ServerConfig build() {
            return new SOCKS5ServerConfig(
                    super.internalBufferSize,
                    super.connectionTimeout,
                    concurrentConnections,
                    concurrentThreadFactory,
                    serverBindPoint,
                    serverBacklog,
                    Map.copyOf(serverAuths),
                    serverRuleset
            );
        }
    }
}
