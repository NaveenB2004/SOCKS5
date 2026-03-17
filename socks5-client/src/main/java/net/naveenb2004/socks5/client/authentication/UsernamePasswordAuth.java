/*
 * Copyright (c) 2026 Naveen N. Balasooriya
 *
 * This source code is licensed under the MIT License.
 * See the LICENSE file in the project root for full license information.
 */

package net.naveenb2004.socks5.client.authentication;

import net.naveenb2004.socks5.client.exception.SOCKS5ClientException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public final class UsernamePasswordAuth extends AbstractClientAuth {
    private final byte[] reqBuffer;

    public UsernamePasswordAuth(final String username,
                                final byte[] password) {
        if (username == null) throw new SOCKS5ClientException("Username cannot be null");
        if (password == null) throw new SOCKS5ClientException("Password cannot be null");

        byte[] usernameBytes = username.getBytes(StandardCharsets.UTF_8);
        if (usernameBytes.length > 255) throw new SOCKS5ClientException("Username is too long");
        if (password.length > 255) throw new SOCKS5ClientException("Password is too long");

        int i = 0;
        reqBuffer = new byte[3 + usernameBytes.length + password.length];
        reqBuffer[i++] = 0x01;
        reqBuffer[i++] = (byte) usernameBytes.length;
        for (byte b : usernameBytes) {
            reqBuffer[i++] = b;
        }
        reqBuffer[i++] = (byte) password.length;
        for (byte b : password) {
            reqBuffer[i++] = b;
        }
    }

    @Override
    public int getAuthMethodId() {
        return 0x02;
    }

    @Override
    public void authenticate(InputStream serverInputStream,
                             OutputStream serverOutputStream) throws IOException, SOCKS5ClientException {
        serverOutputStream.write(reqBuffer);
        serverOutputStream.flush();

        byte[] respBuffer = new byte[2];
        int readBytes = serverInputStream.read(respBuffer);
        if (readBytes != respBuffer.length) {
            if (readBytes == -1) throw new SOCKS5ClientException("Connection closed");
            throw new SOCKS5ClientException("Error while reading server response");
        }

        int version = respBuffer[0] & 0xff;
        if (version != 0x01) throw new SOCKS5ClientException("Invalid username password authentication version from server");

        int status = respBuffer[1] & 0xff;
        if (status != 0x00) throw new SOCKS5ClientException("Authentication failed (status: " + status + ")");
    }
}
