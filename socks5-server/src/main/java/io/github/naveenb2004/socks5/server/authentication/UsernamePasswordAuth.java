/*
 * Copyright (c) 2026 Naveen N. Balasooriya
 *
 * This source code is licensed under the MIT License.
 * See the LICENSE file in the project root for full license information.
 */

package io.github.naveenb2004.socks5.server.authentication;

import io.github.naveenb2004.socks5.server.exception.SOCKS5ServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public final class UsernamePasswordAuth extends AbstractServerAuth {
    private static final Logger LOGGER = LoggerFactory.getLogger(UsernamePasswordAuth.class);

    private final Map<byte[], byte[]> users = new HashMap<>(1, 1);

    public UsernamePasswordAuth() {
    }

    public UsernamePasswordAuth addUser(final String username,
                                        final byte[] password) {
        if (username == null || password == null) throw new SOCKS5ServerException("Username/Password cannot be null");
        byte[] usernameBytes = username.getBytes(StandardCharsets.UTF_8);
        if (usernameBytes.length > 255 || password.length > 255) {
            throw new SOCKS5ServerException("Username/Password cannot be longer than 255 characters");
        }
        if (users.containsKey(usernameBytes)) {
            LOGGER.atWarn().log("User with username '{}' replaced with a new password", username);
        }
        users.put(usernameBytes, password);
        return this;
    }

    @Override
    public int getAuthMethodId() {
        return 0x02;
    }

    @Override
    public void authenticate(InputStream clientInputStream,
                             OutputStream clientOutputStream) throws SOCKS5ServerException, IOException {
        int version = clientInputStream.read();
        if (version != 0x01) {
            if (version == -1) throw new SOCKS5ServerException("Connection closed");
            throw new SOCKS5ServerException("Invalid username password authentication version from client");
        }

        int usernameLength = clientInputStream.read();
        if (usernameLength == -1) throw new SOCKS5ServerException("Connection closed");

        byte[] username = new byte[usernameLength];
        int fetchedUsernameLength = clientInputStream.read(username);
        if (fetchedUsernameLength != usernameLength) {
            if (fetchedUsernameLength == -1) throw new SOCKS5ServerException("Connection closed");
            throw new SOCKS5ServerException("Error while fetching username");
        }

        int passwordLength = clientInputStream.read();
        if (passwordLength == -1) throw new SOCKS5ServerException("Connection closed");

        byte[] password = new byte[passwordLength];
        int fetchedPasswordLength = clientInputStream.read(password);
        if (fetchedPasswordLength != passwordLength) {
            if (fetchedPasswordLength == -1) throw new SOCKS5ServerException("Connection closed");
            throw new SOCKS5ServerException("Error while fetching password");
        }

        byte[] targetPassword = users.get(username);
        byte[] respBuffer = {0x01, 0x00};
        if (targetPassword == null) {
            respBuffer[1] = 0x01;
            clientOutputStream.write(respBuffer);
            clientOutputStream.flush();
            throw new SOCKS5ServerException("User not found");
        }

        if (!Arrays.equals(password, targetPassword)) {
            respBuffer[1] = 0x02;
            clientOutputStream.write(respBuffer);
            clientOutputStream.flush();
            throw new SOCKS5ServerException("User authentication failed (invalid password)");
        }

        clientOutputStream.write(respBuffer);
        clientOutputStream.flush();
    }
}
