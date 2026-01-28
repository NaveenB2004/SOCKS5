package io.github.naveenb2004.socks5.base.exception;

public final class SOCKS5MethodException extends RuntimeException {
    public SOCKS5MethodException() {
    }

    public SOCKS5MethodException(String message) {
        super(message);
    }

    public SOCKS5MethodException(String message,
                                 Throwable cause) {
        super(message, cause);
    }

    public SOCKS5MethodException(Throwable cause) {
        super(cause);
    }
}
