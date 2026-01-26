package io.github.naveenb2004.socks5.server.service;

public class ServerServiceException extends RuntimeException {
    public ServerServiceException() {
    }

    public ServerServiceException(String message) {
        super(message);
    }

    public ServerServiceException(String message,
                                  Throwable cause) {
        super(message, cause);
    }

    public ServerServiceException(Throwable cause) {
        super(cause);
    }

    public ServerServiceException(String message,
                                  Throwable cause,
                                  boolean enableSuppression,
                                  boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
