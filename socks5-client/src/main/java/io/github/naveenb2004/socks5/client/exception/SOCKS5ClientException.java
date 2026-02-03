package io.github.naveenb2004.socks5.client.exception;

import io.github.naveenb2004.socks5.base.SOCKS5Exception;

public sealed class SOCKS5ClientException extends SOCKS5Exception
        permits SOCKS5ClientConfigException {
    public SOCKS5ClientException() {
        super();
    }

    public SOCKS5ClientException(String message) {
        super(message);
    }

    public SOCKS5ClientException(String message,
                                 Throwable cause) {
        super(message, cause);
    }

    public SOCKS5ClientException(Throwable cause) {
        super(cause);
    }
}
