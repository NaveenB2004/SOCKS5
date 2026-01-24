package io.github.naveenb2004.socks5.server;

import io.github.naveenb2004.socks5.server.auth.SOCKS5ServerAuth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.TrustManager;
import java.net.InetAddress;
import java.security.SecureRandom;
import java.util.concurrent.ThreadFactory;

public record SOCKS5ServerConfiguration(int socketPort,
                                        int socketBacklog,
                                        InetAddress socketBindAddress,
                                        ThreadFactory threadFactory,
                                        SOCKS5ServerAuth socks5ServerAuth,
                                        boolean enableSsl,
                                        String sslContextProtocol,
                                        String sslContextProtocolProvider,
                                        KeyManager[] keyManagers,
                                        TrustManager[] trustManagers,
                                        SecureRandom secureRandom,
                                        SSLParameters sslParameters) {

    public static SOCKS5ServerConfigurationBuilder builder() {
        return new SOCKS5ServerConfigurationBuilder();
    }

    public static class SOCKS5ServerConfigurationBuilder {
        private static final Logger LOGGER = LoggerFactory.getLogger(SOCKS5ServerConfigurationBuilder.class);

        private int socketPort;
        private int socketBacklog;
        private InetAddress socketBindAddress;
        private ThreadFactory threadFactory;
        private SOCKS5ServerAuth socks5ServerAuth;
        private boolean enableSsl;
        private String sslContextProtocol;
        private String sslContextProtocolProvider;
        private KeyManager[] keyManagers;
        private TrustManager[] trustManagers;
        private SecureRandom secureRandom;
        private SSLParameters sslParameters;

        private SOCKS5ServerConfigurationBuilder() {
        }

        public SOCKS5ServerConfigurationBuilder socketPort(int socketPort) {
            this.socketPort = socketPort;
            return this;
        }

        public SOCKS5ServerConfigurationBuilder socketBacklog(int socketBacklog) {
            this.socketBacklog = socketBacklog;
            return this;
        }

        public SOCKS5ServerConfigurationBuilder socketBindAddress(InetAddress socketBindAddress) {
            this.socketBindAddress = socketBindAddress;
            return this;
        }

        public SOCKS5ServerConfigurationBuilder threadFactory(ThreadFactory threadFactory) {
            this.threadFactory = threadFactory;
            return this;
        }

        public SOCKS5ServerConfigurationBuilder socks5ServerAuth(SOCKS5ServerAuth socks5ServerAuth) {
            this.socks5ServerAuth = socks5ServerAuth;
            return this;
        }

        public SOCKS5ServerConfigurationBuilder enableSsl(boolean enableSsl) {
            this.enableSsl = enableSsl;
            return this;
        }

        public SOCKS5ServerConfigurationBuilder sslContextProtocol(String sslContextProtocol) {
            this.sslContextProtocol = sslContextProtocol;
            return this;
        }

        public SOCKS5ServerConfigurationBuilder sslContextProtocolProvider(String sslContextProtocolProvider) {
            this.sslContextProtocolProvider = sslContextProtocolProvider;
            return this;
        }

        public SOCKS5ServerConfigurationBuilder keyManagers(KeyManager[] keyManagers) {
            this.keyManagers = keyManagers;
            return this;
        }

        public SOCKS5ServerConfigurationBuilder trustManagers(TrustManager[] trustManagers) {
            this.trustManagers = trustManagers;
            return this;
        }

        public SOCKS5ServerConfigurationBuilder secureRandom(SecureRandom secureRandom) {
            this.secureRandom = secureRandom;
            return this;
        }

        public SOCKS5ServerConfigurationBuilder sslParameters(SSLParameters sslParameters) {
            this.sslParameters = sslParameters;
            return this;
        }

        public SOCKS5ServerConfiguration build() {
            LOGGER.atDebug().log("SOCKS5ServerConfiguration:");
            LOGGER.atDebug().setMessage("@ socketPort: {}").addArgument(socketPort).log();
            LOGGER.atDebug().setMessage("@ socketBacklog: {}").addArgument(socketBacklog).log();
            LOGGER.atDebug().setMessage("@ socketBindAddress: {}").addArgument(socketBindAddress).log();
            LOGGER.atDebug().setMessage("@ threadFactory: {}").addArgument(threadFactory).log();
            LOGGER.atDebug().setMessage("@ socks5ServerAuth: {}").addArgument(socks5ServerAuth).log();
            LOGGER.atDebug().setMessage("@ enableSsl: {}").addArgument(enableSsl).log();
            LOGGER.atDebug().setMessage("@ sslContextProtocol: {}").addArgument(sslContextProtocol).log();
            LOGGER.atDebug().setMessage("@ sslContextProtocolProvider: {}").addArgument(sslContextProtocolProvider).log();
            LOGGER.atDebug().setMessage("@ keyManagers: {}").addArgument(keyManagers).log();
            LOGGER.atDebug().setMessage("@ trustManagers: {}").addArgument(trustManagers).log();
            LOGGER.atDebug().setMessage("@ secureRandom: {}").addArgument(secureRandom).log();
            LOGGER.atDebug().setMessage("@ sslParameters: {}").addArgument(sslParameters).log();

            return new SOCKS5ServerConfiguration(
                    socketPort,
                    socketBacklog,
                    socketBindAddress,
                    threadFactory,
                    socks5ServerAuth,
                    enableSsl,
                    sslContextProtocol,
                    sslContextProtocolProvider,
                    keyManagers,
                    trustManagers,
                    secureRandom,
                    sslParameters
            );
        }
    }
}
