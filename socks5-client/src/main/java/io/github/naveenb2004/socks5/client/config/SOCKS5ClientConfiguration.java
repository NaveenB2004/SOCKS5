package io.github.naveenb2004.socks5.client.config;

import io.github.naveenb2004.socks5.base.ImmutableObject;
import io.github.naveenb2004.socks5.client.exception.SOCKS5ClientConfigException;
import io.github.naveenb2004.socks5.client.method.NoAuthentication;
import io.github.naveenb2004.socks5.client.method.SOCKS5ClientMethod;

import java.net.InetAddress;
import java.util.*;

@ImmutableObject
public final class SOCKS5ClientConfiguration {
    private final InetAddress serverAddress;
    private final int serverPort;
    private final InetAddress localAddress;
    private final int localPort;
    private final Map<Byte, SOCKS5ClientMethod> socks5Methods;

    private SOCKS5ClientConfiguration(InetAddress serverAddress,
                                      int serverPort,
                                      InetAddress localAddress,
                                      int localPort,
                                      Map<Byte, SOCKS5ClientMethod> socks5Methods) {
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

    public Map<Byte, SOCKS5ClientMethod> getSOCKS5Methods() {
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
        private final Set<SOCKS5ClientMethod> socks5ClientMethods = new HashSet<>();

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

        public SOCKS5ClientConfigurationBuilder addSocks5Methods(SOCKS5ClientMethod socks5ClientMethod) throws SOCKS5ClientConfigException {
            if (socks5ClientMethod == null) throw new SOCKS5ClientConfigException("SOCKS5 methods cannot be null");
            this.socks5ClientMethods.add(socks5ClientMethod);
            return this;
        }

        public SOCKS5ClientConfiguration build() throws SOCKS5ClientConfigException {
            if (serverAddress == null) throw new SOCKS5ClientConfigException("Server address not set");
            if (serverPort < 1 || serverPort > 65535) throw new SOCKS5ClientConfigException("Server port out of range");
            if (socks5ClientMethods.isEmpty()) socks5ClientMethods.add(NoAuthentication.builder().build());
            Map<Byte, SOCKS5ClientMethod> socks5MethodsMap = new HashMap<>(socks5ClientMethods.size(), 1);
            for (SOCKS5ClientMethod socks5ClientMethod : socks5ClientMethods)
                socks5MethodsMap.put(socks5ClientMethod.getMethodId(), socks5ClientMethod);
            return new SOCKS5ClientConfiguration(serverAddress, serverPort, localAddress, localPort, socks5MethodsMap);
        }
    }
}
