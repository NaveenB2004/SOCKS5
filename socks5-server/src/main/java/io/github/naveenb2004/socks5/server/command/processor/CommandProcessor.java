package io.github.naveenb2004.socks5.server.command.processor;

import io.github.naveenb2004.socks5.base.ATYP;
import io.github.naveenb2004.socks5.base.REP;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;

public interface CommandProcessor {
    void process();

    static void sendResponse(OutputStream outputStream,
                             REP reply,
                             ATYP addressType,
                             InetAddress bindAddress,
                             int bindPort) throws IOException {
        outputStream.write(0x05);
        outputStream.write(reply.getValue());
        outputStream.write(0x00);
        outputStream.write(addressType.getValue());
        outputStream.write(bindAddress.getAddress());
        outputStream.write((bindPort >>> 8) & 0xFF);
        outputStream.write((bindPort & 0xFF));
    }
}
