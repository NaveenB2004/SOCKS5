package io.github.naveenb2004.socks5.server.exception;

public final class SOCKS5ServerConfigException extends SOCKS5ServerException {
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
