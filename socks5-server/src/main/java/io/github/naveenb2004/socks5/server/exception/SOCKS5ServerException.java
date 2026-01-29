package io.github.naveenb2004.socks5.server.exception;

import io.github.naveenb2004.socks5.base.exception.SOCKS5Exception;

public sealed class SOCKS5ServerException extends SOCKS5Exception
        permits SOCKS5ServerConfigException {
    public SOCKS5ServerException() {
        super();
    }

    public SOCKS5ServerException(String message) {
        super(message);
    }

    public SOCKS5ServerException(String message,
                                 Throwable cause) {
        super(message, cause);
    }

    public SOCKS5ServerException(Throwable cause) {
        super(cause);
    }
}
