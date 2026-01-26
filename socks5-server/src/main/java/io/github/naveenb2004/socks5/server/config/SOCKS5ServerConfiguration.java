package io.github.naveenb2004.socks5.server.config;

import io.github.naveenb2004.socks5.server.auth.method.NoAuthentication;
import io.github.naveenb2004.socks5.server.auth.SOCKS5ServerAuth;
import io.github.naveenb2004.socks5.server.exception.SOCKS5ServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.TrustManager;
import java.net.InetAddress;
import java.security.SecureRandom;
import java.util.List;
import java.util.concurrent.ThreadFactory;

public final class SOCKS5ServerConfiguration {
    private final int socketPort;
    private final int socketBacklog;
    private final InetAddress socketBindAddress;
    private final ThreadFactory threadFactory;
    private final int maximumClients;
    private final List<? extends SOCKS5ServerAuth> socks5ServerAuths;
    private final SOCKS5ServerRuleset socks5ServerRuleset;
    private final boolean sslEnabled;
    private final String sslContextProtocol;
    private final String sslContextProtocolProvider;
    private final KeyManager[] keyManagers;
    private final TrustManager[] trustManagers;
    private final SecureRandom secureRandom;
    private final SSLParameters sslParameters;

    private SOCKS5ServerConfiguration(int socketPort,
                                      int socketBacklog,
                                      InetAddress socketBindAddress,
                                      ThreadFactory threadFactory,
                                      int maximumClients,
                                      List<? extends SOCKS5ServerAuth> socks5ServerAuths,
                                      SOCKS5ServerRuleset socks5ServerRuleset,
                                      boolean sslEnabled,
                                      String sslContextProtocol,
                                      String sslContextProtocolProvider,
                                      KeyManager[] keyManagers,
                                      TrustManager[] trustManagers,
                                      SecureRandom secureRandom,
                                      SSLParameters sslParameters) {
        this.socketPort = socketPort;
        this.socketBacklog = socketBacklog;
        this.socketBindAddress = socketBindAddress;
        this.threadFactory = threadFactory;
        this.maximumClients = maximumClients;
        this.socks5ServerAuths = socks5ServerAuths;
        this.socks5ServerRuleset = socks5ServerRuleset;
        this.sslEnabled = sslEnabled;
        this.sslContextProtocol = sslContextProtocol;
        this.sslContextProtocolProvider = sslContextProtocolProvider;
        this.keyManagers = keyManagers;
        this.trustManagers = trustManagers;
        this.secureRandom = secureRandom;
        this.sslParameters = sslParameters;
    }

    public int getSocketPort() {
        return socketPort;
    }

    public int getSocketBacklog() {
        return socketBacklog;
    }

    public InetAddress getSocketBindAddress() {
        return socketBindAddress;
    }

    public ThreadFactory getThreadFactory() {
        return threadFactory;
    }

    public int getMaximumClients() {
        return maximumClients;
    }

    public List<? extends SOCKS5ServerAuth> getSocks5ServerAuths() {
        return socks5ServerAuths;
    }

    public SOCKS5ServerRuleset getSocks5ServerRuleset() {
        return socks5ServerRuleset;
    }

    public boolean isSslEnabled() {
        return sslEnabled;
    }

    public String getSslContextProtocol() {
        return sslContextProtocol;
    }

    public String getSslContextProtocolProvider() {
        return sslContextProtocolProvider;
    }

    public KeyManager[] getKeyManagers() {
        return keyManagers;
    }

    public TrustManager[] getTrustManagers() {
        return trustManagers;
    }

    public SecureRandom getSecureRandom() {
        return secureRandom;
    }

    public SSLParameters getSslParameters() {
        return sslParameters;
    }

    public static SOCKS5ServerConfigurationBuilder builder() {
        return new SOCKS5ServerConfigurationBuilder();
    }

    public static final class SOCKS5ServerConfigurationBuilder {
        private static final Logger LOGGER = LoggerFactory.getLogger(SOCKS5ServerConfigurationBuilder.class);

        private int socketPort = 1080;
        private int socketBacklog = 50;
        private InetAddress socketBindAddress;
        private ThreadFactory threadFactory = Thread.ofVirtual().factory();
        private int maximumClients = 100;
        private List<? extends SOCKS5ServerAuth> socks5ServerAuths = List.of(new NoAuthentication());
        private SOCKS5ServerRuleset socks5ServerRuleset;
        private boolean sslEnabled;
        private String sslContextProtocol = "TLSv1.3";
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

        public SOCKS5ServerConfigurationBuilder maximumClients(int maximumClients) {
            this.maximumClients = maximumClients;
            return this;
        }

        public SOCKS5ServerConfigurationBuilder socks5ServerAuth(List<? extends SOCKS5ServerAuth> socks5ServerAuths) {
            this.socks5ServerAuths = socks5ServerAuths;
            return this;
        }

        public SOCKS5ServerConfigurationBuilder socks5ServerRuleset(SOCKS5ServerRuleset socks5ServerRuleset) {
            this.socks5ServerRuleset = socks5ServerRuleset;
            return this;
        }

        public SOCKS5ServerConfigurationBuilder sslEnabled(boolean sslEnabled) {
            this.sslEnabled = sslEnabled;
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

        private void validateAndSet() {
            if (socketPort < 0 || socketPort > 65535) throw new SOCKS5ServerException("Port out of range: " + socketPort);
            if (maximumClients < 1) throw new SOCKS5ServerException("Maximum client count must be >= 1");
            if (socks5ServerAuths == null) throw new SOCKS5ServerException("Socks5ServerAuth array is null");
            if (sslEnabled && sslContextProtocol == null) throw new SOCKS5ServerException("SSL context protocol is null");
        }

        public SOCKS5ServerConfiguration build() {
            LOGGER.atDebug().log("SOCKS5ServerConfiguration:");
            LOGGER.atDebug().setMessage("@ socketPort: {}").addArgument(socketPort).log();
            LOGGER.atDebug().setMessage("@ socketBacklog: {}").addArgument(socketBacklog).log();
            LOGGER.atDebug().setMessage("@ socketBindAddress: {}").addArgument(socketBindAddress).log();
            LOGGER.atDebug().setMessage("@ threadFactory: {}").addArgument(threadFactory).log();
            LOGGER.atDebug().setMessage("@ maximumClients: {}").addArgument(maximumClients).log();
            LOGGER.atDebug().setMessage("@ socks5ServerAuths: {}").addArgument(socks5ServerAuths).log();
            LOGGER.atDebug().setMessage("@ socks5ServerRuleset: {}").addArgument(socks5ServerRuleset).log();
            LOGGER.atDebug().setMessage("@ sslEnabled: {}").addArgument(sslEnabled).log();
            LOGGER.atDebug().setMessage("@ sslContextProtocol: {}").addArgument(sslContextProtocol).log();
            LOGGER.atDebug().setMessage("@ sslContextProtocolProvider: {}").addArgument(sslContextProtocolProvider).log();
            LOGGER.atDebug().setMessage("@ keyManagers: {}").addArgument(keyManagers).log();
            LOGGER.atDebug().setMessage("@ trustManagers: {}").addArgument(trustManagers).log();
            LOGGER.atDebug().setMessage("@ secureRandom: {}").addArgument(secureRandom).log();
            LOGGER.atDebug().setMessage("@ sslParameters: {}").addArgument(sslParameters).log();

            validateAndSet();
            return new SOCKS5ServerConfiguration(
                    socketPort,
                    socketBacklog,
                    socketBindAddress,
                    threadFactory,
                    maximumClients,
                    socks5ServerAuths,
                    socks5ServerRuleset,
                    sslEnabled,
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
