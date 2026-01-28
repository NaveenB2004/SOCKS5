package io.github.naveenb2004.socks5.client;

import io.github.naveenb2004.socks5.base.Immutable;
import io.github.naveenb2004.socks5.base.method.SOCKS5Methods;
import io.github.naveenb2004.socks5.base.method.impl.NoAuthentication;
import io.github.naveenb2004.socks5.client.exception.SOCKS5ClientException;

import java.net.InetAddress;

@Immutable
public final class SOCKS5ClientConfiguration {
    private final InetAddress serverAddress;
    private final int serverPort;
    private final InetAddress localAddress;
    private final int localPort;
    private final SOCKS5Methods socks5Methods;

    private SOCKS5ClientConfiguration(InetAddress serverAddress,
                                      int serverPort,
                                      InetAddress localAddress,
                                      int localPort,
                                      SOCKS5Methods socks5Methods) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.localAddress = localAddress;
        this.localPort = localPort;
        this.socks5Methods = socks5Methods;
    }

    public InetAddress getServerAddress() {
        return serverAddress;
    }

    public int getServerPort() {
        return serverPort;
    }

    public InetAddress getLocalAddress() {
        return localAddress;
    }

    public int getLocalPort() {
        return localPort;
    }

    public SOCKS5Methods getSOCKS5Methods() {
        return socks5Methods;
    }

    public static SOCKS5ClientConfigurationBuilder builder() {
        return new SOCKS5ClientConfigurationBuilder();
    }

    public static class SOCKS5ClientConfigurationBuilder {
        private InetAddress serverAddress;
        private int serverPort;
        private InetAddress localAddress;
        private int localPort;
        private SOCKS5Methods socks5Methods;

        private SOCKS5ClientConfigurationBuilder() {
        }

        public SOCKS5ClientConfigurationBuilder serverAddress(InetAddress serverAddress) {
            this.serverAddress = serverAddress;
            return this;
        }

        public SOCKS5ClientConfigurationBuilder serverPort(int serverPort) {
            this.serverPort = serverPort;
            return this;
        }

        public SOCKS5ClientConfigurationBuilder localAddress(InetAddress localAddress) {
            this.localAddress = localAddress;
            return this;
        }

        public SOCKS5ClientConfigurationBuilder localPort(int localPort) {
            if (localPort < 0 || localPort > 65535) throw new SOCKS5ClientException("Local port out of range");
            this.localPort = localPort;
            return this;
        }

        public SOCKS5ClientConfigurationBuilder socks5Methods(SOCKS5Methods socks5Methods) {
            if (socks5Methods == null) throw new SOCKS5ClientException("SOCKS5 methods cannot be null");
            this.socks5Methods = socks5Methods;
            return this;
        }

        public SOCKS5ClientConfiguration build() {
            if (serverAddress == null) throw new SOCKS5ClientException("Server address not set");
            if (serverPort < 1 || serverPort > 65535) throw new SOCKS5ClientException("Server port out of range");
            if (socks5Methods == null) socks5Methods = SOCKS5Methods.builder().addMethod(new NoAuthentication()).build();
            return new SOCKS5ClientConfiguration(serverAddress, serverPort, localAddress, localPort, socks5Methods);
        }
    }
}
