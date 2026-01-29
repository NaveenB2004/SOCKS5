package io.github.naveenb2004.socks5.client.command.request;

import io.github.naveenb2004.socks5.client.command.response.SOCKS5Response;

public interface SOCKS5Request {
    SOCKS5Response execute();
}
