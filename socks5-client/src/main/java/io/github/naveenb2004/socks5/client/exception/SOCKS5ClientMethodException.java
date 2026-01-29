package io.github.naveenb2004.socks5.client.exception;

import io.github.naveenb2004.socks5.base.exception.SOCKS5MethodException;

public final class SOCKS5ClientMethodException extends SOCKS5MethodException {
    public SOCKS5ClientMethodException() {
        super();
    }

    public SOCKS5ClientMethodException(String message) {
        super(message);
    }

    public SOCKS5ClientMethodException(String message,
                                       Throwable cause) {
        super(message, cause);
    }

    public SOCKS5ClientMethodException(Throwable cause) {
        super(cause);
    }
}
