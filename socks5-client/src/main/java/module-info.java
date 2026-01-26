module io.github.naveenb2004.socks5.client {
    requires org.slf4j;
    requires transitive io.github.naveenb2004.socks5.base;

    exports io.github.naveenb2004.socks5.client;
    exports io.github.naveenb2004.socks5.client.auth;
}