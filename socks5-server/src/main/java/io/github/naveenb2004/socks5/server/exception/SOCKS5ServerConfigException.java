package io.github.naveenb2004.socks5.server.exception;

import io.github.naveenb2004.socks5.base.exception.SOCKS5ConfigException;

public class SOCKS5ServerConfigException extends SOCKS5ConfigException {
    public SOCKS5ServerConfigException() {
        super();
    }

    public SOCKS5ServerConfigException(String message) {
        super(message);
    }

    public SOCKS5ServerConfigException(String message,
                                       Throwable cause) {
        super(message, cause);
    }

    public SOCKS5ServerConfigException(Throwable cause) {
        super(cause);
    }
}
