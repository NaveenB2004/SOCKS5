package io.github.naveenb2004.socks5.client;

public class SOCKS5ClientException extends RuntimeException {
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

    protected SOCKS5ClientException(String message,
                                    Throwable cause,
                                    boolean enableSuppression,
                                    boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
