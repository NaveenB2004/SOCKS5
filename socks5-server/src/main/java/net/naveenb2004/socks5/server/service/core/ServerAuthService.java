/*
 * Copyright (c) 2026 Naveen N. Balasooriya
 *
 * This source code is licensed under the MIT License.
 * See the LICENSE file in the project root for full license information.
 */

package net.naveenb2004.socks5.server.service.core;

import net.naveenb2004.socks5.base.template.AuthRequest;
import net.naveenb2004.socks5.base.template.AuthResponse;
import net.naveenb2004.socks5.server.exception.SOCKS5ServerException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class ServerAuthService {
    public static AuthRequest receiveAuthSelectionRequest(final InputStream inputStream) throws IOException {
        int version = inputStream.read();
        if (version != 0x05) {
            if (version == -1) throw new SOCKS5ServerException("Connection closed");
            throw new SOCKS5ServerException("Invalid SOCK version from client");
        }

        int methodsCount = inputStream.read();
        if (methodsCount == -1) throw new SOCKS5ServerException("Connection closed");

        int[] methodIds = new int[methodsCount];
        for (int i = 0; i < methodsCount; i++) {
            methodIds[i] = inputStream.read();
            if (methodIds[i] == -1) throw new SOCKS5ServerException("Connection closed");
        }

        return new AuthRequest(methodIds);
    }

    public static void sendAuthSelectionResponse(final OutputStream outputStream,
                                                 final AuthResponse response) throws IOException {
        outputStream.write(0x05);
        outputStream.write(response.authMethodId());
    }
}
