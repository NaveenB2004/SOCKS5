module io.github.naveenb2004.socks5.server {
    requires org.slf4j;
    requires transitive io.github.naveenb2004.socks5.base;

    exports io.github.naveenb2004.socks5.server;
    exports io.github.naveenb2004.socks5.server.auth;
    exports io.github.naveenb2004.socks5.server.config;
    exports io.github.naveenb2004.socks5.server.endpoint;
    exports io.github.naveenb2004.socks5.server.exception;
    exports io.github.naveenb2004.socks5.server.auth.method;
}