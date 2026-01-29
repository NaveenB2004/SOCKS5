package io.github.naveenb2004.socks5.base.exception;

public class SOCKS5Exception extends RuntimeException {
    public SOCKS5Exception() {
    }

    public SOCKS5Exception(String message) {
        super(message);
    }

    public SOCKS5Exception(String message,
                           Throwable cause) {
        super(message, cause);
    }

    public SOCKS5Exception(Throwable cause) {
        super(cause);
    }
}
