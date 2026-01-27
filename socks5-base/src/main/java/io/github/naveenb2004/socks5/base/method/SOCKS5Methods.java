package io.github.naveenb2004.socks5.base.method;

import io.github.naveenb2004.socks5.base.exception.SOCKS5ConfigException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public final class SOCKS5Methods {
    private static final Logger LOGGER = LoggerFactory.getLogger(SOCKS5Methods.class);

    private final Map<Byte, SOCKS5MethodImpl> methodImpls;

    private SOCKS5Methods(List<SOCKS5MethodImpl> methods) {
        methodImpls = new HashMap<>(methods.size(), 1);
        for (SOCKS5MethodImpl method : methods) {
            methodImpls.put(method.getMethodId(), method);
            LOGGER.atDebug().log("SOCKS5 method added: {}", method.getMethodId());
        }
    }

    public Map<Byte, SOCKS5MethodImpl> getMethodImpls() {
        return Collections.unmodifiableMap(methodImpls);
    }

    public static final class SOCKS5MethodsBuilder {
        private final List<SOCKS5MethodImpl> methodImpls;

        private SOCKS5MethodsBuilder() {
            methodImpls = new ArrayList<>();
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
