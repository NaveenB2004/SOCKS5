package io.github.naveenb2004.socks5.client;

import javax.net.ssl.KeyManager;
import javax.net.ssl.TrustManager;
import java.net.InetAddress;
import java.security.SecureRandom;

public record SOCKS5ClientConfiguration(int socketPort,
                                        int socketBacklog,
                                        InetAddress socketBindAddress,
                                        boolean enableSsl,
                                        String sslContextProtocol,
                                        String sslContextProtocolProvider,
                                        KeyManager[] keyManagers,
                                        TrustManager[] trustManagers,
                                        SecureRandom secureRandom) {

    public static SOCKS5ClientConfigurationBuilder builder() {
        return new SOCKS5ClientConfigurationBuilder();
    }

    public static class SOCKS5ClientConfigurationBuilder {
        private int socketPort;
        private int socketBacklog;
        private InetAddress socketBindAddress;
        private boolean enableSsl;
        private String sslContextProtocol;
        private String sslContextProtocolProvider;
        private KeyManager[] keyManagers;
        private TrustManager[] trustManagers;
        private SecureRandom secureRandom;

        private SOCKS5ClientConfigurationBuilder() {
        }

        public SOCKS5ClientConfigurationBuilder socketPort(int socketPort) {
            this.socketPort = socketPort;
            return this;
        }

        public SOCKS5ClientConfigurationBuilder socketBacklog(int socketBacklog) {
            this.socketBacklog = socketBacklog;
            return this;
        }

        public SOCKS5ClientConfigurationBuilder socketBindAddress(InetAddress socketBindAddress) {
            this.socketBindAddress = socketBindAddress;
            return this;
        }

        public SOCKS5ClientConfigurationBuilder enableSsl(boolean enableSsl) {
            this.enableSsl = enableSsl;
            return this;
        }

        public SOCKS5ClientConfigurationBuilder sslContextProtocol(String sslContextProtocol) {
            this.sslContextProtocol = sslContextProtocol;
            return this;
        }

        public SOCKS5ClientConfigurationBuilder sslContextProtocolProvider(String sslContextProtocolProvider) {
            this.sslContextProtocolProvider = sslContextProtocolProvider;
            return this;
        }

        public SOCKS5ClientConfigurationBuilder keyManagers(KeyManager[] keyManagers) {
            this.keyManagers = keyManagers;
            return this;
        }

        public SOCKS5ClientConfigurationBuilder trustManagers(TrustManager[] trustManagers) {
            this.trustManagers = trustManagers;
            return this;
        }

        public SOCKS5ClientConfigurationBuilder secureRandom(SecureRandom secureRandom) {
            this.secureRandom = secureRandom;
            return this;
        }

        public SOCKS5ClientConfiguration build() {
            return new SOCKS5ClientConfiguration(
                    socketPort,
                    socketBacklog,
                    socketBindAddress,
                    enableSsl,
                    sslContextProtocol,
                    sslContextProtocolProvider,
                    keyManagers,
                    trustManagers,
                    secureRandom
            );
        }
    }
}
