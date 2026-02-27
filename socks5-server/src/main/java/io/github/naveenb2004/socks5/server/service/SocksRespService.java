/*
 * Copyright (c) 2026 Naveen N. Balasooriya
 *
 * This source code is licensed under the MIT License.
 * See the LICENSE file in the project root for full license information.
 */

package io.github.naveenb2004.socks5.server.service;

import io.github.naveenb2004.socks5.base.AddressType;
import io.github.naveenb2004.socks5.base.Command;
import io.github.naveenb2004.socks5.base.template.SocksRequest;
import io.github.naveenb2004.socks5.server.exception.SOCKS5ServerException;

import java.io.IOException;
import java.io.InputStream;

public final class SocksRespService {
    public static SocksRequest receiveConnReq(final InputStream inputStream) throws IOException {
        int version = inputStream.read();
        if (version != 0x05) {
            if (version == -1) throw new SOCKS5ServerException("Connection closed");
            throw new SOCKS5ServerException("Invalid SOCKS version from client");
        }

        int commandByte = inputStream.read();
        Command command = Command.fromValue(commandByte);
        if (command == null) {
            if (commandByte == -1) throw new SOCKS5ServerException("Connection closed");
            throw new SOCKS5ServerException("Invalid command from client");
        }

        int rsv = inputStream.read();
        if (rsv != 0x00) {
            if (rsv == -1) throw new SOCKS5ServerException("Connection closed");
            throw new SOCKS5ServerException("Invalid RSV from client");
        }

        int addressTypeByte = inputStream.read();
        AddressType addressType = AddressType.fromValue(addressTypeByte);
        if (addressType == null) {
            if (addressTypeByte == -1) throw new SOCKS5ServerException("Connection closed");
            throw new SOCKS5ServerException("Invalid Address Type from client");
        }

        byte[] destAddress = switch (addressType) {
            case IPv4 -> new byte[4];
            case DOMAIN_NAME -> {
                int addressLength = inputStream.read();
                if (addressLength == -1) throw new SOCKS5ServerException("Connection closed");
                yield new byte[addressLength];
            }
            case IPv6 -> new byte[16];
        };
        int destAddressLength = inputStream.read(destAddress);
        if (destAddressLength != destAddress.length) {
            if (destAddressLength == -1) throw new SOCKS5ServerException("Connection closed");
            throw new SOCKS5ServerException("Error while reading Destination Address from client");
        }

        byte[] destPortBytes = new byte[2];
        int destPortLength = inputStream.read(destPortBytes);
        if (destPortLength != destPortBytes.length) {
            if (destPortLength == -1) throw new SOCKS5ServerException("Connection closed");
            throw new SOCKS5ServerException("Error while reading Destination Port from client");
        }
        int destPort = ((destPortBytes[0] & 0xff) << 16) | ((destPortBytes[1] & 0xff) << 8);

        return new SocksRequest(command, addressType, destAddress, destPort);
    }
}
