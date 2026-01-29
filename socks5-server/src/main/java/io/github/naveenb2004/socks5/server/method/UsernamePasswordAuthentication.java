package io.github.naveenb2004.socks5.server.method;

import io.github.naveenb2004.socks5.base.Immutable;
import io.github.naveenb2004.socks5.base.exception.SOCKS5ConfigException;
import io.github.naveenb2004.socks5.server.exception.SOCKS5ServerException;
import io.github.naveenb2004.socks5.server.exception.SOCKS5ServerServiceException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Immutable
public final class UsernamePasswordAuthentication implements SOCKS5ServerMethod {
    private final Map<String, String> unamePsswdMap;

    private UsernamePasswordAuthentication(Map<String, String> unamePsswdMap) {
        this.unamePsswdMap = unamePsswdMap;
    }

    @Override
    public byte getMethodId() {
        return 0x02;
    }

    @Override
    public void negotiateAsServer(InputStream inputStream,
                                  OutputStream outputStream) throws SOCKS5ServerServiceException {
        try {
            int subNegotiationVer = inputStream.read();
            if (subNegotiationVer != 0x01) throw new SOCKS5ServerServiceException("Invalid negotiation version");

            int unameLen = inputStream.read();
            byte[] unameBytes = new byte[unameLen];
            int readUlen = inputStream.read(unameBytes);
            if (unameLen != readUlen) throw new SOCKS5ServerServiceException("Error reading negotiation data");

            int passwdLen = inputStream.read();
            byte[] passwdBytes = new byte[passwdLen];
            int readPasswdLen = inputStream.read(passwdBytes);
            if (passwdLen != readPasswdLen) throw new SOCKS5ServerServiceException("Error reading negotiation data");

            String clientUname = new String(unameBytes, StandardCharsets.UTF_8);
            String clientPasswd = new String(passwdBytes, StandardCharsets.UTF_8);

            String passwd = unamePsswdMap.get(clientUname);
            if (!clientPasswd.equals(passwd)) {
                outputStream.write(0x01);
                outputStream.write(0x01);
                outputStream.flush();
                throw new SOCKS5ServerServiceException("Invalid username/password");
            }

            outputStream.write(0x01);
            outputStream.write(0x00);
            outputStream.flush();
        } catch (IOException | SOCKS5ServerException e) {
            throw new SOCKS5ServerServiceException(e);
        }
    }

    @Override
    public InputStream setupDecapsulationAsServer(InputStream inputStream) {
        return inputStream;
    }

    @Override
    public OutputStream setupEncapsulationAsServer(OutputStream outputStream) {
        return outputStream;
    }

    public static UsernamePasswordAuthenticationBuilder builder() {
        return new UsernamePasswordAuthenticationBuilder();
    }

    public static final class UsernamePasswordAuthenticationBuilder {
        private final Map<String, String> unamePsswdMap = new HashMap<>();

        private UsernamePasswordAuthenticationBuilder() {
        }

        public void addUnamePasswd(String uname,
                                   String passwd) throws SOCKS5ConfigException {
            if (uname == null || uname.isBlank()) {
                throw new SOCKS5ConfigException("Username cannot be null or blank");
            }
            if (passwd == null) throw new SOCKS5ConfigException("Password cannot be null");
            if (uname.length() > 255) throw new SOCKS5ConfigException("Username too long");
            if (passwd.length() > 255) throw new SOCKS5ConfigException("Password too long");
            unamePsswdMap.put(uname, passwd);
        }

        public SOCKS5ServerMethod build() throws SOCKS5ConfigException {
            if (this.unamePsswdMap.isEmpty()) throw new SOCKS5ConfigException("No username and password provided");
            return new UsernamePasswordAuthentication(Map.copyOf(unamePsswdMap));
        }
    }
}
