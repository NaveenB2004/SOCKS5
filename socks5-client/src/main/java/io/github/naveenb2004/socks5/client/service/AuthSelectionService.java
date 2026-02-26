/*
 * Copyright (c) 2026 Naveen N. Balasooriya
 *
 * This source code is licensed under the MIT License.
 * See the LICENSE file in the project root for full license information.
 */

package io.github.naveenb2004.socks5.client.service;

import io.github.naveenb2004.socks5.base.template.AuthSelectionRequest;
import io.github.naveenb2004.socks5.base.template.AuthSelectionResponse;
import io.github.naveenb2004.socks5.client.exception.SOCKS5ClientException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class AuthSelectionService {
    public static void sendAuthSelectionReq(final OutputStream outputStream,
                                            final AuthSelectionRequest authSelectionRequest) throws IOException {
        outputStream.write(0x05);
        outputStream.write(authSelectionRequest.authMethodIds().length);
        for (int authMethod : authSelectionRequest.authMethodIds()) {
            outputStream.write(authMethod);
        }
    }

    public static AuthSelectionResponse receiveAuthSelectionResp(final InputStream inputStream) throws IOException {
        int version = inputStream.read();
        if (version != 0x05) {
            if (version == -1) throw new SOCKS5ClientException("Connection closed");
            throw new SOCKS5ClientException("Invalid SOCK version from server");
        }

        int methodId = inputStream.read();
        if (methodId == -1) throw new SOCKS5ClientException("Connection closed");

        return new AuthSelectionResponse(methodId);
    }
}
