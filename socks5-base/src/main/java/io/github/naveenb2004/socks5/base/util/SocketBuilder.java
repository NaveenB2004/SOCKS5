package io.github.naveenb2004.socks5.base.util;

import javax.net.ssl.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.security.SecureRandom;

public final class SocketBuilder {
    public static Socket build(InetAddress address,
                               int port) {
        try {
            return new Socket(address, port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Socket build(InetAddress address,
                               int port,
                               String sslContextProtocol,
                               String sslContextProtocolProvider,
                               KeyManager[] keyManagers,
                               TrustManager[] trustManagers,
                               SecureRandom secureRandom,
                               SSLParameters sslParameters) {
        try {
            SSLContext sslContext = sslContextProtocolProvider == null ?
                    SSLContext.getInstance(sslContextProtocol) :
                    SSLContext.getInstance(sslContextProtocol, sslContextProtocolProvider);
            sslContext.init(keyManagers, trustManagers, secureRandom);
            SSLSocketFactory sslServerSocketFactory = sslContext.getSocketFactory();
            SSLSocket sslSocket = (SSLSocket) sslServerSocketFactory.createSocket(address, port);
                sslSocket.setSSLParameters(sslParameters);
                return sslSocket;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
