/*
 * Copyright (c) 2026 Naveen N. Balasooriya
 *
 * This source code is licensed under the MIT License.
 * See the LICENSE file in the project root for full license information.
 */

package net.naveenb2004.socks5.server.exception;

import net.naveenb2004.socks5.base.exception.SOCKS5Exception;

public final class SOCKS5ServerException extends SOCKS5Exception {
    public SOCKS5ServerException() {
        super();
    }

    public SOCKS5ServerException(String message) {
        super(message);
    }

    public SOCKS5ServerException(String message,
                                 Throwable cause) {
        super(message, cause);
    }

    public SOCKS5ServerException(Throwable cause) {
        super(cause);
    }
}
