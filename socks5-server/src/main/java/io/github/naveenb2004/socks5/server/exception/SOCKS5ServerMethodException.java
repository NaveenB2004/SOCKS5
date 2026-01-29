package io.github.naveenb2004.socks5.server.exception;

import io.github.naveenb2004.socks5.base.exception.SOCKS5MethodException;

public final class SOCKS5ServerMethodException extends SOCKS5MethodException {
    public SOCKS5ServerMethodException() {
        super();
    }

    public SOCKS5ServerMethodException(String message) {
        super(message);
    }

    public SOCKS5ServerMethodException(String message,
                                       Throwable cause) {
        super(message, cause);
    }

    public SOCKS5ServerMethodException(Throwable cause) {
        super(cause);
    }
}
