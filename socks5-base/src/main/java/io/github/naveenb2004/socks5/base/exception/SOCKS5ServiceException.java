package io.github.naveenb2004.socks5.base.exception;

public class SOCKS5ServiceException extends Exception {
    public SOCKS5ServiceException() {
    }

    public SOCKS5ServiceException(String message) {
        super(message);
    }

    public SOCKS5ServiceException(String message,
                                  Throwable cause) {
        super(message, cause);
    }

    public SOCKS5ServiceException(Throwable cause) {
        super(cause);
    }
}
