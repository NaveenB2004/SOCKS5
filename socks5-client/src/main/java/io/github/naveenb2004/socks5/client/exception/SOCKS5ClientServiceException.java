package io.github.naveenb2004.socks5.client.exception;

import io.github.naveenb2004.socks5.base.exception.SOCKS5ServiceException;

public final class SOCKS5ClientServiceException extends SOCKS5ServiceException {
    public SOCKS5ClientServiceException() {
        super();
    }

    public SOCKS5ClientServiceException(String message) {
        super(message);
    }

    public SOCKS5ClientServiceException(String message,
                                        Throwable cause) {
        super(message, cause);
    }

    public SOCKS5ClientServiceException(Throwable cause) {
        super(cause);
    }
}
