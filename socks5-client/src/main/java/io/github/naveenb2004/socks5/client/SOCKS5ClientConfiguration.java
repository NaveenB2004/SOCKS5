package io.github.naveenb2004.socks5.client;

import io.github.naveenb2004.socks5.base.Immutable;
import io.github.naveenb2004.socks5.base.method.NoAuthentication;
import io.github.naveenb2004.socks5.base.method.SOCKS5Method;
import io.github.naveenb2004.socks5.client.exception.SOCKS5ClientException;

import java.net.InetAddress;
import java.util.*;

@Immutable
public final class SOCKS5ClientConfiguration {
    private final InetAddress serverAddress;
    private final int serverPort;
    private final InetAddress localAddress;
    private final int localPort;
    private final List<SOCKS5Method> socks5Methods;

    private SOCKS5ClientConfiguration(InetAddress serverAddress,
                                      int serverPort,
                                      InetAddress localAddress,
                                      int localPort,
                                      List<SOCKS5Method> socks5Methods) {
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

    public List<SOCKS5Method> getSOCKS5Methods() {
        return Collections.unmodifiableList(socks5Methods);
    }

    public static SOCKS5ClientConfigurationBuilder builder() {
        return new SOCKS5ClientConfigurationBuilder();
    }

    public static class SOCKS5ClientConfigurationBuilder {
        private InetAddress serverAddress;
        private int serverPort;
        private InetAddress localAddress;
        private int localPort;
        private SequencedSet<SOCKS5Method> socks5Methods = new LinkedHashSet<>();

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

        public SOCKS5ClientConfigurationBuilder addSocks5Methods(SOCKS5Method socks5Method) {
            if (socks5Method == null) throw new SOCKS5ClientException("SOCKS5 methods cannot be null");
            this.socks5Methods.add(socks5Method);
            return this;
        }

        public SOCKS5ClientConfiguration build() {
            if (serverAddress == null) throw new SOCKS5ClientException("Server address not set");
            if (serverPort < 1 || serverPort > 65535) throw new SOCKS5ClientException("Server port out of range");
            if (socks5Methods.isEmpty()) socks5Methods.add(new NoAuthentication());
            List<SOCKS5Method> socks5MethodsList = new ArrayList<>(socks5Methods);
            return new SOCKS5ClientConfiguration(serverAddress, serverPort, localAddress, localPort, socks5MethodsList);
        }
    }
}
