/*
 * Copyright (c) 2026 Naveen N. Balasooriya
 *
 * This source code is licensed under the MIT License.
 * See the LICENSE file in the project root for full license information.
 */

package io.github.naveenb2004.socks5.base.exception;

public abstract class AbstractSOCKS5Exception extends RuntimeException {
    public AbstractSOCKS5Exception() {
        super();
    }

    public AbstractSOCKS5Exception(String message) {
        super(message);
    }

    public AbstractSOCKS5Exception(String message,
                                   Throwable cause) {
        super(message, cause);
    }

    public AbstractSOCKS5Exception(Throwable cause) {
        super(cause);
    }
}
