package io.github.naveenb2004.socks5.client.exception;

public final class SOCKS5ClientConfigException extends SOCKS5ClientException {
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
