package io.github.naveenb2004.socks5.server.endpoint;

import java.net.Socket;

public record SOCKS5ClientConnection(Socket socket) {
}
