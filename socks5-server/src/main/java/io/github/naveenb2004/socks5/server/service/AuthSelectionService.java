/*
 * Copyright (c) 2026 Naveen N. Balasooriya
 *
 * This source code is licensed under the MIT License.
 * See the LICENSE file in the project root for full license information.
 */

package io.github.naveenb2004.socks5.server.service;

import io.github.naveenb2004.socks5.base.template.AuthSelectionRequest;
import io.github.naveenb2004.socks5.base.template.AuthSelectionResponse;
import io.github.naveenb2004.socks5.server.exception.SOCKS5ServerException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class AuthSelectionService {
    public static AuthSelectionRequest receiveAuthSelectionRequest(final InputStream inputStream) throws IOException {
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

        return new AuthSelectionRequest(methodIds);
    }

    public static void sendAuthSelectionResponse(final OutputStream outputStream,
                                                 final AuthSelectionResponse response) throws IOException {
        outputStream.write(0x05);
        outputStream.write(response.authMethodId());
    }
}
