package io.github.naveenb2004.socks5.base.method;

import io.github.naveenb2004.socks5.base.exception.SOCKS5ConfigException;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public final class UsernamePasswordAuthentication implements SOCKS5Method {
    private Map<String, String> usernamePasswordMap;

    private UsernamePasswordAuthentication(Map<String, String> usernamePasswordMap) {
        this.usernamePasswordMap = usernamePasswordMap;
    }

    @Override
    public byte getMethodId() {
        return 0x02;
    }

    @Override
    public void negotiate(InputStream inputStream,
                          OutputStream outputStream) {
    }

    @Override
    public InputStream setupDecapsulation(InputStream inputStream) {
        return inputStream;
    }

    @Override
    public OutputStream setupEncapsulation(OutputStream outputStream) {
        return outputStream;
    }

    public static class UsernamePasswordAuthenticationBuilder {
        private final Map<String, String> usernamePasswordMap = new HashMap<>();

        private UsernamePasswordAuthenticationBuilder() {
        }

        public void addUsernamePassword(String username, String password) throws SOCKS5ConfigException {
            if (username == null || username.isBlank())  {
                throw new SOCKS5ConfigException("Username cannot be null or blank");
            }
            if (password == null) throw new SOCKS5ConfigException("Password cannot be null");
            usernamePasswordMap.put(username, password);
        }

        public SOCKS5Method build() throws SOCKS5ConfigException {
            if (this.usernamePasswordMap.isEmpty())  throw new SOCKS5ConfigException("No username and password provided");
            Map<String, String> usernamePasswordMap = new HashMap<>(this.usernamePasswordMap);
            return new UsernamePasswordAuthentication(usernamePasswordMap);
        }
    }
}
