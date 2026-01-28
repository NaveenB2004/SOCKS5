package io.github.naveenb2004.socks5.base.method;

import io.github.naveenb2004.socks5.base.Immutable;

@Immutable
public record MethodResponse(SOCKS5Methods method) {
}
