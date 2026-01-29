package io.github.naveenb2004.socks5.base.exception;

public class SOCKS5ConfigException extends RuntimeException {
    public SOCKS5ConfigException() {
        super();
    }

    public SOCKS5ConfigException(String message) {
        super(message);
    }

    public SOCKS5ConfigException(String message,
                                 Throwable cause) {
        super(message, cause);
    }

    public SOCKS5ConfigException(Throwable cause) {
        super(cause);
    }
}
