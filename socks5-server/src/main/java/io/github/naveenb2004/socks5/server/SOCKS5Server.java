package io.github.naveenb2004.socks5.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SOCKS5Server {
    private static final Logger LOGGER = LoggerFactory.getLogger(SOCKS5Server.class);
    private final String serverIdentifier;
    private final SOCKS5ServerConfiguration configuration;

    private SOCKS5Server(String serverIdentifier,
                         SOCKS5ServerConfiguration configuration) {
        this.serverIdentifier = serverIdentifier;
        this.configuration = configuration;
    }

    public void init() {
        LOGGER.atInfo().setMessage("Initializing SOCKS5Server ({})").addArgument(serverIdentifier).log();

    }

    public void destroy() {
        LOGGER.atInfo().setMessage("Destroying SOCKS5Server ({})").addArgument(serverIdentifier).log();

    }

    public SOCKS5ServerConfiguration getConfiguration() {
        return configuration;
    }

    public static SOCKS5Server newInstance(String serverIdentifier,
                                           SOCKS5ServerConfiguration configuration) {
        return new SOCKS5Server(serverIdentifier, configuration);
    }

    public static SOCKS5Server newInstance(SOCKS5ServerConfiguration configuration) {
        return new SOCKS5Server("", configuration);
    }
}
