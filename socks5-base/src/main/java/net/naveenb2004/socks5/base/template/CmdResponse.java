/*
 * Copyright (c) 2026 Naveen N. Balasooriya
 *
 * This source code is licensed under the MIT License.
 * See the LICENSE file in the project root for full license information.
 */

package net.naveenb2004.socks5.base.template;

import net.naveenb2004.socks5.base.AddressType;
import net.naveenb2004.socks5.base.Reply;

public record CmdResponse(Reply reply,
                          AddressType addressType,
                          byte[] bindAddress,
                          int bindPort) {
}
