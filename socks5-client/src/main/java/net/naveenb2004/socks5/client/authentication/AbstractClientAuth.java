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

public abstract class AbstractClientAuth {
    public abstract int getAuthMethodId();

    public abstract void authenticate(final InputStream serverInputStream,
                                      final OutputStream serverOutputStream) throws IOException, SOCKS5ClientException;
}
