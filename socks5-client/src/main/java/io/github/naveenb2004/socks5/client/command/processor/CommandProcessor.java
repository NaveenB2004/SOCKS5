package io.github.naveenb2004.socks5.client.command.processor;

import io.github.naveenb2004.socks5.base.ATYP;
import io.github.naveenb2004.socks5.base.CMD;
import io.github.naveenb2004.socks5.client.command.SOCKS5Response;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public interface CommandProcessor {
    SOCKS5Response process();

    static void sendRequest(OutputStream outputStream,
                            CMD command,
                            ATYP addressType,
                            InetSocketAddress destination) throws IOException {
        outputStream.write(0x05);
        outputStream.write(command.getValue());
        outputStream.write(0x00);
        outputStream.write(addressType.getValue());
        switch (addressType) {
            case IP_V4_ADDRESS, IP_V6_ADDRESS -> outputStream.write(destination.getAddress().getAddress());
            case DOMAINNAME -> {
                outputStream.write(destination.getHostName().length());
                outputStream.write(destination.getHostName().getBytes());
            }
        }
        outputStream.write((destination.getPort() >>> 8) & 0xFF);
        outputStream.write((destination.getPort() & 0xFF));
    }
}
