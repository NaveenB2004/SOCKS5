/*
 * Copyright (c) 2026 Naveen N. Balasooriya
 *
 * This source code is licensed under the MIT License.
 * See the LICENSE file in the project root for full license information.
 */

package net.naveenb2004.socks5.server.service.cmd;

import net.naveenb2004.socks5.base.AddressType;
import net.naveenb2004.socks5.base.Reply;
import net.naveenb2004.socks5.base.template.CmdRequest;
import net.naveenb2004.socks5.base.template.CmdResponse;
import net.naveenb2004.socks5.server.exception.SOCKS5ServerException;
import net.naveenb2004.socks5.server.service.core.ServerCmdService;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

public abstract sealed class CmdHandler permits Connect, Bind, UdpAssociate {
    public abstract void handle() throws IOException, InterruptedException;

    static InetSocketAddress getDestination(final CmdRequest clientRequest,
                                            final OutputStream clientOutputStream) throws IOException {
        InetSocketAddress destination;
        try {
            destination = switch (clientRequest.addressType()) {
                case IPv4, IPv6 -> {
                    final var inetAddr = InetAddress.getByAddress(clientRequest.destAddress());
                    yield new InetSocketAddress(inetAddr, clientRequest.destPort());
                }
                case DOMAIN_NAME -> new InetSocketAddress(new String(clientRequest.destAddress()), clientRequest.destPort());
            };
        } catch (UnknownHostException uhe) {
            var cmdResp = new CmdResponse(Reply.GENERAL_SOCKS_SERVER_FAILURE, AddressType.DOMAIN_NAME, new byte[]{0x00}, 0);
            ServerCmdService.sendCmdResp(clientOutputStream, cmdResp);
            throw new SOCKS5ServerException("Unable to connect to the destination.", uhe);
        }
        return destination;
    }
}
