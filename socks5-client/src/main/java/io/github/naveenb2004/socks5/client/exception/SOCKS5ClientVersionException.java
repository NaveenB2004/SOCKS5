package io.github.naveenb2004.socks5.client.exception;

import io.github.naveenb2004.socks5.base.exception.SOCKS5VersionException;

public class SOCKS5ClientVersionException extends SOCKS5VersionException {
    public SOCKS5ClientVersionException() {
        super();
    }

    public SOCKS5ClientVersionException(String message) {
        super(message);
    }

    public SOCKS5ClientVersionException(String message,
                                        Throwable cause) {
        super(message, cause);
    }

    public SOCKS5ClientVersionException(Throwable cause) {
        super(cause);
    }
}
