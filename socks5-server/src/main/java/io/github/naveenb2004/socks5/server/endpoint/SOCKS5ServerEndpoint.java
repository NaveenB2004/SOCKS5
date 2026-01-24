package io.github.naveenb2004.socks5.server.endpoint;

import java.net.ServerSocket;
import java.net.Socket;

public interface SOCKS5ServerEndpoint {
    void onServerInitializing(ServerSocket serverSocket);
    void onClientConnected(SOCKS5ClientConnection client);
}
