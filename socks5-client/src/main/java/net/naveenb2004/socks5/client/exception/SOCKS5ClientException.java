/*
 * Copyright (c) 2026 Naveen N. Balasooriya
 *
 * This source code is licensed under the MIT License.
 * See the LICENSE file in the project root for full license information.
 */

package net.naveenb2004.socks5.client.exception;

import net.naveenb2004.socks5.base.exception.SOCKS5Exception;

public final class SOCKS5ClientException extends SOCKS5Exception {
    public SOCKS5ClientException() {
        super();
    }

    public SOCKS5ClientException(String message) {
        super(message);
    }

    public SOCKS5ClientException(String message,
                                 Throwable cause) {
        super(message, cause);
    }

    public SOCKS5ClientException(Throwable cause) {
        super(cause);
    }
}
