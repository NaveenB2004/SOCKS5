package io.github.naveenb2004.socks5.server;

import io.github.naveenb2004.socks5.server.config.SOCKS5ServerConfiguration;
import io.github.naveenb2004.socks5.server.service.SocketInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SOCKS5Server {
    private static final Logger LOGGER = LoggerFactory.getLogger(SOCKS5Server.class);

    private final String serverIdentifier;
    private final SOCKS5ServerConfiguration configuration;
    private final SocketInitializer socketInitializer;

    private SOCKS5Server(String serverIdentifier,
                         SOCKS5ServerConfiguration configuration) {
        this.serverIdentifier = serverIdentifier;
        this.configuration = configuration;
        this.socketInitializer = new SocketInitializer(configuration);
    }

    public String getServerIdentifier() {
        return serverIdentifier;
    }

    public SOCKS5ServerConfiguration getConfiguration() {
        return configuration;
    }

    public void init() {
        LOGGER.atInfo().setMessage("Initializing SOCKS5Server ({})").addArgument(serverIdentifier).log();
        socketInitializer.init();
    }

    public void destroy(long gracefulPeriod) {
        LOGGER.atInfo().setMessage("Destroying SOCKS5Server ({})").addArgument(serverIdentifier).log();
        socketInitializer.destroy(gracefulPeriod);
    }

    public void destroy() {
        destroy(0);
    }

    public static SOCKS5ServerBuilder builder() {
        return new SOCKS5ServerBuilder();
    }

    public static final class SOCKS5ServerBuilder {
        private static final Logger LOGGER = LoggerFactory.getLogger(SOCKS5ServerBuilder.class);

        private String serverIdentifier;
        private SOCKS5ServerConfiguration configuration;

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

        private void validateAndSet() {
            if (serverIdentifier == null) serverIdentifier = "UNNAMED";
            if (configuration == null) throw new SOCKS5ServerException("Configuration cannot be null");
        }

        public SOCKS5Server build() {
            LOGGER.atDebug().log("SOCKS5Server:");
            LOGGER.atDebug().setMessage("@ serverIdentifier: {}").addArgument(serverIdentifier).log();
            LOGGER.atDebug().setMessage("@ configuration: {}").addArgument(configuration).log();

            validateAndSet();
            return new SOCKS5Server(
                    serverIdentifier,
                    configuration
            );
        }
    }
}
