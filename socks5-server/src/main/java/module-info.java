module net.naveenb2004.socks5.server {
    requires org.slf4j;
    requires transitive net.naveenb2004.socks5.base;

    exports net.naveenb2004.socks5.server;
    exports net.naveenb2004.socks5.server.authentication;
    exports net.naveenb2004.socks5.server.configuration;
    exports net.naveenb2004.socks5.server.exception;
}