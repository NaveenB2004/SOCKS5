package io.github.naveenb2004.socks5.base.exception;

public class SOCKS5VersionException extends RuntimeException {
    public SOCKS5VersionException() {
    }

    public SOCKS5VersionException(String message) {
        super(message);
    }

    public SOCKS5VersionException(String message,
                                  Throwable cause) {
        super(message, cause);
    }

    public SOCKS5VersionException(Throwable cause) {
        super(cause);
    }
}
