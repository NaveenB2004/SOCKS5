/*
 * Copyright (c) 2026 Naveen N. Balasooriya
 *
 * This source code is licensed under the MIT License.
 * See the LICENSE file in the project root for full license information.
 */

package io.github.naveenb2004.socks5.base.template;

import io.github.naveenb2004.socks5.base.AddressType;
import io.github.naveenb2004.socks5.base.Command;

import java.net.InetSocketAddress;

public record ConnectionRequest(Command command,
                                AddressType addressType,
                                InetSocketAddress destinationInfo) {
}
