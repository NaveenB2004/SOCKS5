module io.github.naveenb2004.socks5.server {
    requires org.slf4j;
    requires transitive io.github.naveenb2004.socks5.base;

    exports io.github.naveenb2004.socks5.server;
    exports io.github.naveenb2004.socks5.server.auth;
    exports io.github.naveenb2004.socks5.server.config;
    exports io.github.naveenb2004.socks5.server.auth.method;
    exports io.github.naveenb2004.socks5.server.service;
    exports io.github.naveenb2004.socks5.server.service.util;
    exports io.github.naveenb2004.socks5.server.service.reqHandler;
}