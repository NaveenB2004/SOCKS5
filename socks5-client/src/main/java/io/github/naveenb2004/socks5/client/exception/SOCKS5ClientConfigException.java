package io.github.naveenb2004.socks5.client.exception;

import io.github.naveenb2004.socks5.base.exception.SOCKS5ConfigException;

public final class SOCKS5ClientConfigException extends SOCKS5ConfigException {
    public SOCKS5ClientConfigException() {
        super();
    }

    public SOCKS5ClientConfigException(String message) {
        super(message);
    }

    public SOCKS5ClientConfigException(String message,
                                       Throwable cause) {
        super(message, cause);
    }

    public SOCKS5ClientConfigException(Throwable cause) {
        super(cause);
    }
}
