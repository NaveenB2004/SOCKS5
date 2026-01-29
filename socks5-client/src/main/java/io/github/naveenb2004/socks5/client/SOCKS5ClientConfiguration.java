package io.github.naveenb2004.socks5.client;

import io.github.naveenb2004.socks5.base.Immutable;
import io.github.naveenb2004.socks5.base.method.NoAuthentication;
import io.github.naveenb2004.socks5.base.method.SOCKS5Method;
import io.github.naveenb2004.socks5.client.exception.SOCKS5ClientConfigException;

import java.net.InetAddress;
import java.util.*;

@Immutable
public final class SOCKS5ClientConfiguration {
    private final InetAddress serverAddress;
    private final int serverPort;
    private final InetAddress localAddress;
    private final int localPort;
    private final Map<Byte, SOCKS5Method> socks5Methods;

    private SOCKS5ClientConfiguration(InetAddress serverAddress,
                                      int serverPort,
                                      InetAddress localAddress,
                                      int localPort,
                                      Map<Byte, SOCKS5Method> socks5Methods) {
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

    public Map<Byte, SOCKS5Method> getSOCKS5Methods() {
        return Collections.unmodifiableMap(socks5Methods);
    }

    public static SOCKS5ClientConfigurationBuilder builder() {
        return new SOCKS5ClientConfigurationBuilder();
    }

    public static class SOCKS5ClientConfigurationBuilder {
        private InetAddress serverAddress;
        private int serverPort;
        private InetAddress localAddress;
        private int localPort;
        private final Set<SOCKS5Method> socks5Methods = new HashSet<>();

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

        public SOCKS5ClientConfigurationBuilder localPort(int localPort) throws SOCKS5ClientConfigException {
            if (localPort < 0 || localPort > 65535) throw new SOCKS5ClientConfigException("Local port out of range");
            this.localPort = localPort;
            return this;
        }

        public SOCKS5ClientConfigurationBuilder addSocks5Methods(SOCKS5Method socks5Method) throws SOCKS5ClientConfigException {
            if (socks5Method == null) throw new SOCKS5ClientConfigException("SOCKS5 methods cannot be null");
            this.socks5Methods.add(socks5Method);
            return this;
        }

        public SOCKS5ClientConfiguration build() throws SOCKS5ClientConfigException {
            if (serverAddress == null) throw new SOCKS5ClientConfigException("Server address not set");
            if (serverPort < 1 || serverPort > 65535) throw new SOCKS5ClientConfigException("Server port out of range");
            if (socks5Methods.isEmpty()) socks5Methods.add(NoAuthentication.builder().build());
            Map<Byte, SOCKS5Method> socks5MethodsMap = new HashMap<>(socks5Methods.size(), 1);
            for (SOCKS5Method socks5Method : socks5Methods) socks5MethodsMap.put(socks5Method.getMethodId(), socks5Method);
            return new SOCKS5ClientConfiguration(serverAddress, serverPort, localAddress, localPort, socks5MethodsMap);
        }
    }
}
