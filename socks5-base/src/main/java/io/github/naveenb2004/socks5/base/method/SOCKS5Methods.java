package io.github.naveenb2004.socks5.base.method;

import io.github.naveenb2004.socks5.base.Immutable;
import io.github.naveenb2004.socks5.base.exception.SOCKS5ConfigException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@Immutable
public final class SOCKS5Methods {
    private static final Logger LOGGER = LoggerFactory.getLogger(SOCKS5Methods.class);

    private final List<? super SOCKS5MethodImpl> methodImpls;

    private SOCKS5Methods(SequencedSet<? super SOCKS5MethodImpl> methods) {
        methodImpls = new ArrayList<>(methods);
    }

    public List<? super SOCKS5MethodImpl> getMethodImpls() {
        return Collections.unmodifiableList(methodImpls);
    }

    public static SOCKS5MethodsBuilder builder() {
        return new SOCKS5MethodsBuilder();
    }

    public static final class SOCKS5MethodsBuilder {
        private final SequencedSet<? super SOCKS5MethodImpl> methodImpls;

        private SOCKS5MethodsBuilder() {
            methodImpls = new LinkedHashSet<>();
        }

        public SOCKS5MethodsBuilder addMethod(SOCKS5MethodImpl method) {
            if (method == null) throw new SOCKS5ConfigException("Method cannot be null");
            methodImpls.add(method);
            return this;
        }

        public SOCKS5Methods build() {
            if (methodImpls.isEmpty()) throw new SOCKS5ConfigException("No SOCKS5MethodImpl(s) defined");
            return new SOCKS5Methods(methodImpls);
        }
    }
}
