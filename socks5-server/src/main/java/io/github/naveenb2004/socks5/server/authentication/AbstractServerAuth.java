/*
 * Copyright (c) 2026 Naveen N. Balasooriya
 *
 * This source code is licensed under the MIT License.
 * See the LICENSE file in the project root for full license information.
 */

package io.github.naveenb2004.socks5.server.authentication;

import io.github.naveenb2004.socks5.server.exception.SOCKS5ServerException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class AbstractServerAuth {
    public abstract int getAuthMethodId();

    public abstract void authenticate(final InputStream clientInputStream,
                                      final OutputStream clientOutputStream) throws IOException, SOCKS5ServerException;
}
