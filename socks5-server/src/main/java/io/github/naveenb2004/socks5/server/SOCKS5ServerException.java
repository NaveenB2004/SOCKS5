package io.github.naveenb2004.socks5.server;

public class SOCKS5ServerException extends RuntimeException {
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

    protected SOCKS5ServerException(String message,
                                    Throwable cause,
                                    boolean enableSuppression,
                                    boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
