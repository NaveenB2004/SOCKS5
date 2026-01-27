package io.github.naveenb2004.socks5.client;

import io.github.naveenb2004.socks5.client.command.response.BindResponse;
import io.github.naveenb2004.socks5.client.command.response.ConnectResponse;
import io.github.naveenb2004.socks5.client.command.response.UdpAssociateResponse;
import io.github.naveenb2004.socks5.client.exception.SOCKS5ClientException;

import java.net.InetAddress;

public final class SOCKS5Client {
    private final InetAddress socks5ServerAddress;
    private final int socks5ServerPort;

    private SOCKS5Client(InetAddress socks5ServerAddress,
                         int socks5ServerPort) {
        this.socks5ServerAddress = socks5ServerAddress;
        this.socks5ServerPort = socks5ServerPort;
    }

    public InetAddress getSocks5ServerAddress() {
        return socks5ServerAddress;
    }

    public int getSocks5ServerPort() {
        return socks5ServerPort;
    }

    public BindResponse bind() {
        return null;
    }

    public ConnectResponse connect() {
        return null;
    }

    public UdpAssociateResponse udpAssociate() {
        return null;
    }

    public static SOCKS5ClientBuilder builder() {
        return new SOCKS5ClientBuilder();
    }

    public static final class SOCKS5ClientBuilder {
        private InetAddress socks5ServerAddress;
        private int socks5ServerPort;

        private SOCKS5ClientBuilder() {
        }

        public SOCKS5ClientBuilder socks5ServerAddress(InetAddress socks5ServerAddress) {
            this.socks5ServerAddress = socks5ServerAddress;
            return this;
        }

        public SOCKS5ClientBuilder socks5Port(int socks5ServerPort) {
            this.socks5ServerPort = socks5ServerPort;
            return this;
        }

        public SOCKS5Client build() {
            if (socks5ServerAddress == null) throw new SOCKS5ClientException("SOCKS5 server address cannot be null");
            if (socks5ServerPort < 1 || socks5ServerPort > 65535) throw new SOCKS5ClientException("SOCKS5 server port out of range");
            return new SOCKS5Client(socks5ServerAddress, socks5ServerPort);
        }
    }
}
