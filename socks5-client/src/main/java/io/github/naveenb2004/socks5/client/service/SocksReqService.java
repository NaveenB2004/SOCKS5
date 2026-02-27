/*
 * Copyright (c) 2026 Naveen N. Balasooriya
 *
 * This source code is licensed under the MIT License.
 * See the LICENSE file in the project root for full license information.
 */

package io.github.naveenb2004.socks5.client.service;

import io.github.naveenb2004.socks5.base.AddressType;
import io.github.naveenb2004.socks5.base.Reply;
import io.github.naveenb2004.socks5.base.template.SocksRequest;
import io.github.naveenb2004.socks5.base.template.SocksResponse;
import io.github.naveenb2004.socks5.client.exception.SOCKS5ClientException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class SocksReqService {
    public static void sendConnReq(final OutputStream outputStream,
                                   final SocksRequest socksRequest) throws IOException {
        outputStream.write(0x05); // VER
        outputStream.write(socksRequest.command().getValue()); // CMD
        outputStream.write(0x00); // RSV
        outputStream.write(socksRequest.addressType().getValue()); // ATYP
        if (socksRequest.addressType() == AddressType.DOMAIN_NAME) {
            outputStream.write(socksRequest.destAddress().length);
        }
        outputStream.write(socksRequest.destAddress()); // DST.ADDR
        outputStream.write(socksRequest.destPort() >>> 8); // DST.PORT (first byte)
        outputStream.write(socksRequest.destPort()); // DST.PORT (second byte)
    }

    public static SocksResponse receiveConnResp(final InputStream inputStream) throws IOException {
        int version = inputStream.read();
        if (version != 0x05) {
            if (version == -1) throw new SOCKS5ClientException("Connection closed");
            throw new SOCKS5ClientException("Invalid SOCK version from server");
        }

        int replyByte = inputStream.read();
        Reply reply = Reply.valueOf(replyByte);
        if (reply == null) {
            if (replyByte == -1) throw new SOCKS5ClientException("Connection closed");
            throw new SOCKS5ClientException("Invalid Reply from server");
        }

        int rsv = inputStream.read();
        if (rsv != 0x00) {
            if (rsv == -1) throw new SOCKS5ClientException("Connection closed");
            throw new SOCKS5ClientException("Invalid RSV from server");
        }

        int addressTypeByte = inputStream.read();
        AddressType addressType = AddressType.fromValue(addressTypeByte);
        if (addressType == null) {
            if (addressTypeByte == -1) throw new SOCKS5ClientException("Connection closed");
            throw new SOCKS5ClientException("Invalid AddressType from server");
        }

        byte[] bindAddress = switch (addressType) {
            case IPv4 -> new byte[4];
            case DOMAIN_NAME -> {
                int addressLength = inputStream.read();
                if (addressLength == -1) throw new SOCKS5ClientException("Connection closed");
                yield new byte[addressLength];
            }
            case IPv6 -> new byte[16];
        };
        int bindAddressLength = inputStream.read(bindAddress);
        if (bindAddressLength != bindAddress.length) {
            if (bindAddressLength == -1) throw new SOCKS5ClientException("Connection closed");
            throw new SOCKS5ClientException("Error while reading Bind Address from server");
        }

        byte[] bindPortBytes = new byte[2];
        int bindPortLength = inputStream.read(bindPortBytes);
        if (bindPortLength != bindPortBytes.length) {
            if (bindPortLength == -1) throw new SOCKS5ClientException("Connection closed");
            throw new SOCKS5ClientException("Error while reading Bind Port from server");
        }
        int bindPort = ((bindPortBytes[0] & 0xff) << 16) | ((bindPortBytes[1] & 0xff) << 8);

        return new SocksResponse(reply, addressType, bindAddress, bindPort);
    }
}
