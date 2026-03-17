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

public final class NoAuth extends AbstractClientAuth {
    public NoAuth() {
    }

    @Override
    public int getAuthMethodId() {
        return 0x00;
    }

    @Override
    public void authenticate(InputStream serverInputStream,
                             OutputStream serverOutputStream) throws IOException, SOCKS5ClientException {
    }
}
