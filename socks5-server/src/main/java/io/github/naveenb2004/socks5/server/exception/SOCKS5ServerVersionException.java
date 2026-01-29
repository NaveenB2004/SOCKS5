package io.github.naveenb2004.socks5.server.exception;

import io.github.naveenb2004.socks5.base.exception.SOCKS5VersionException;

public class SOCKS5ServerVersionException extends SOCKS5VersionException {
    public SOCKS5ServerVersionException() {
        super();
    }

    public SOCKS5ServerVersionException(String message) {
        super(message);
    }

    public SOCKS5ServerVersionException(String message,
                                        Throwable cause) {
        super(message, cause);
    }

    public SOCKS5ServerVersionException(Throwable cause) {
        super(cause);
    }
}
