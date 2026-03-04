/*
 * Copyright (c) 2026 Naveen N. Balasooriya
 *
 * This source code is licensed under the MIT License.
 * See the LICENSE file in the project root for full license information.
 */

package io.github.naveenb2004.socks5.client.authentication;

import io.github.naveenb2004.socks5.client.exception.SOCKS5ClientException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public final class UsernamePasswordAuth extends AbstractClientAuth {
    private final byte[] username;
    private final byte[] password;

    public UsernamePasswordAuth(final String username,
                                final byte[] password) {
        this.username = username.getBytes(StandardCharsets.UTF_8);
        this.password = password;
    }

    @Override
    public int getAuthMethodId() {
        return 0x02;
    }

    @Override
    public void authenticate(InputStream serverInputStream,
                             OutputStream serverOutputStream) throws IOException, SOCKS5ClientException {
        serverOutputStream.write(0x01);
        serverOutputStream.write(username.length);
        serverOutputStream.write(username);
        serverOutputStream.write(password.length);
        serverOutputStream.write(password);
        serverOutputStream.flush();

        int version = serverInputStream.read();
        if (version != 0x01) {
            if (version == -1) throw new SOCKS5ClientException("Connection closed");
            throw new SOCKS5ClientException("Invalid username password authentication version from server");
        }

        int status = serverInputStream.read();
        if (status != 0x00) {
            if (status == -1) throw new SOCKS5ClientException("Connection closed");
            throw new SOCKS5ClientException("Authentication failed (status: " + status + ")");
        }
    }
}
