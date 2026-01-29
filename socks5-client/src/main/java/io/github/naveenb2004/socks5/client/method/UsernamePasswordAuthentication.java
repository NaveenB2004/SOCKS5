package io.github.naveenb2004.socks5.client.method;

import io.github.naveenb2004.socks5.base.Immutable;
import io.github.naveenb2004.socks5.base.exception.SOCKS5ConfigException;
import io.github.naveenb2004.socks5.client.exception.SOCKS5ClientServiceException;
import io.github.naveenb2004.socks5.base.method.SOCKS5Method;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

@Immutable
public final class UsernamePasswordAuthentication implements SOCKS5Method {
    private final String uname;
    private final String passwd;

    private UsernamePasswordAuthentication(String uname,
                                           String passwd) {
        this.uname = uname;
        this.passwd = passwd;
    }

    @Override
    public byte getMethodId() {
        return 0x02;
    }

    @Override
    public void negotiate(InputStream inputStream,
                          OutputStream outputStream) throws SOCKS5ClientServiceException {
        try {
            outputStream.write(0x01);
            outputStream.write(uname.length());
            outputStream.write(uname.getBytes(StandardCharsets.UTF_8));
            outputStream.write(passwd.length());
            outputStream.write(passwd.getBytes(StandardCharsets.UTF_8));
            outputStream.flush();

            int subNegotiationVer = inputStream.read();
            if (subNegotiationVer != 0x01) throw new SOCKS5ClientServiceException("Invalid negotiation version");
            int status = inputStream.read();
            if (status != 0x00) throw new SOCKS5ClientServiceException("Negotiation failed with status: " + status);
        } catch (IOException | SOCKS5ClientServiceException e) {
            throw new SOCKS5ClientServiceException(e);
        }
    }

    @Override
    public InputStream setupDecapsulation(InputStream inputStream) {
        return inputStream;
    }

    @Override
    public OutputStream setupEncapsulation(OutputStream outputStream) {
        return outputStream;
    }

    public static UsernamePasswordAuthenticationBuilder builder() {
        return new UsernamePasswordAuthenticationBuilder();
    }

    public static final class UsernamePasswordAuthenticationBuilder {
        private String uname;
        private String passwd;

        private UsernamePasswordAuthenticationBuilder() {
        }

        public UsernamePasswordAuthenticationBuilder uname(String uname) {
            this.uname = uname;
            return this;
        }

        public UsernamePasswordAuthenticationBuilder password(String passwd) {
            this.passwd = passwd;
            return this;
        }

        public SOCKS5Method build() throws SOCKS5ConfigException {
            if (uname == null || uname.isBlank() || passwd == null) {
                throw new SOCKS5ConfigException("Invalid username and/or password");
            }
            if (uname.length() > 255) throw new SOCKS5ConfigException("Username too long");
            if (passwd.length() > 255) throw new SOCKS5ConfigException("Password too long");
            return new UsernamePasswordAuthentication(uname, passwd);
        }
    }
}
