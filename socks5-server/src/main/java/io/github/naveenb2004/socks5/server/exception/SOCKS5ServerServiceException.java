package io.github.naveenb2004.socks5.server.exception;

import io.github.naveenb2004.socks5.base.exception.SOCKS5ServiceException;

public final class SOCKS5ServerServiceException extends SOCKS5ServiceException {
    public SOCKS5ServerServiceException() {
        super();
    }

    public SOCKS5ServerServiceException(String message) {
        super(message);
    }

    public SOCKS5ServerServiceException(String message,
                                        Throwable cause) {
        super(message, cause);
    }

    public SOCKS5ServerServiceException(Throwable cause) {
        super(cause);
    }
}
