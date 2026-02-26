/*
 * Copyright (c) 2026 Naveen N. Balasooriya
 *
 * This source code is licensed under the MIT License.
 * See the LICENSE file in the project root for full license information.
 */

package io.github.naveenb2004.socks5.client.authentication;

public final class NoAuthentication extends AbstractClientAuthentication {
    @Override
    public int getAuthMethodId() {
        return 0x00;
    }
}
