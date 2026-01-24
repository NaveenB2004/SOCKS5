package io.github.naveenb2004.socks5.server;

import io.github.naveenb2004.socks5.server.endpoint.SOCKS5ServerEndpoint;
import io.github.naveenb2004.socks5.server.exception.SOCKS5ServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SOCKS5Server {
    private static final Logger LOGGER = LoggerFactory.getLogger(SOCKS5Server.class);

    private final String serverIdentifier;
    private final SOCKS5ServerConfiguration configuration;
    private final SOCKS5ServerEndpoint serverEndpoint;

    private SOCKS5Server(String serverIdentifier,
                         SOCKS5ServerConfiguration configuration,
                         SOCKS5ServerEndpoint serverEndpoint) {
        this.serverIdentifier = serverIdentifier;
        this.configuration = configuration;
        this.serverEndpoint = serverEndpoint;
    }

    public String getServerIdentifier() {
        return serverIdentifier;
    }

    public SOCKS5ServerConfiguration getConfiguration() {
        return configuration;
    }

    public SOCKS5ServerEndpoint getServerEndpoint() {
        return serverEndpoint;
    }

    public void init() {
        LOGGER.atInfo().setMessage("Initializing SOCKS5Server ({})").addArgument(serverIdentifier).log();

    }

    public void destroy() {
        LOGGER.atInfo().setMessage("Destroying SOCKS5Server ({})").addArgument(serverIdentifier).log();

    }

    public static SOCKS5ServerBuilder builder() {
        return new SOCKS5ServerBuilder();
    }

    public static final class SOCKS5ServerBuilder {
        private static final Logger LOGGER = LoggerFactory.getLogger(SOCKS5ServerBuilder.class);

        private String serverIdentifier;
        private SOCKS5ServerConfiguration configuration;
        private SOCKS5ServerEndpoint serverEndpoint;

        private SOCKS5ServerBuilder() {
        }

        public SOCKS5ServerBuilder serverIdentifier(String serverIdentifier) {
            this.serverIdentifier = serverIdentifier;
            return this;
        }

        public SOCKS5ServerBuilder configuration(SOCKS5ServerConfiguration configuration) {
            this.configuration = configuration;
            return this;
        }

        public SOCKS5ServerBuilder serverEndpoint(SOCKS5ServerEndpoint serverEndpoint) {
            this.serverEndpoint = serverEndpoint;
            return this;
        }

        private void validateAndSet() {
            if (serverIdentifier == null) serverIdentifier = "UNNAMED";
            if (configuration == null) throw new SOCKS5ServerException("Configuration cannot be null");
            if (serverEndpoint == null) throw new SOCKS5ServerException("ServerEndpoint cannot be null");
        }

        public SOCKS5Server build() {
            LOGGER.atDebug().log("SOCKS5Server:");
            LOGGER.atDebug().setMessage("@ serverIdentifier: {}").addArgument(serverIdentifier).log();
            LOGGER.atDebug().setMessage("@ configuration: {}").addArgument(configuration).log();
            LOGGER.atDebug().setMessage("@ serverEndpoint: {}").addArgument(serverEndpoint).log();

            validateAndSet();
            return new SOCKS5Server(
                    serverIdentifier,
                    configuration,
                    serverEndpoint
            );
        }
    }
}
