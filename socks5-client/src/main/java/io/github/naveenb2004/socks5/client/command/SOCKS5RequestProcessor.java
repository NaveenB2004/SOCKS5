package io.github.naveenb2004.socks5.client.command;

import io.github.naveenb2004.socks5.base.ATYP;
import io.github.naveenb2004.socks5.base.command.CMD;
import io.github.naveenb2004.socks5.base.command.CommandRequest;
import io.github.naveenb2004.socks5.base.command.CommandResponse;
import io.github.naveenb2004.socks5.base.command.REP;
import io.github.naveenb2004.socks5.client.command.response.SOCKS5Response;
import io.github.naveenb2004.socks5.client.exception.SOCKS5ClientException;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public abstract sealed class SOCKS5RequestProcessor
        permits BindRequestProcessor, ConnectRequestProcessor, UdpAssociateRequestProcessor {
    protected final CMD command;
    protected final Socket socks5Server;
    protected final InetSocketAddress destination;

    public SOCKS5RequestProcessor(CMD command,
                                  Socket socks5Server,
                                  InetSocketAddress destination) {
        this.command = command;
        this.socks5Server = socks5Server;
        this.destination = destination;
    }

    public abstract SOCKS5Response execute();

    public static void sendRequest(OutputStream outputStream,
                                   CommandRequest commandRequest) {
        try {
            outputStream.write(0x05);
            outputStream.write(commandRequest.cmd().getValue());
            outputStream.write(0x00);
            outputStream.write(commandRequest.atyp().getValue());
            if (commandRequest.atyp() == ATYP.DOMAINNAME) {
                outputStream.write(commandRequest.dest().getHostName().length());
                outputStream.write(commandRequest.dest().getHostName().getBytes(StandardCharsets.UTF_8));
            } else {
                outputStream.write(commandRequest.dest().getAddress().getAddress());
            }
            outputStream.write((commandRequest.dest().getPort() >>> 8) & 0xff);
            outputStream.write(commandRequest.dest().getPort() & 0xff);
        } catch (Exception e) {
            throw new SOCKS5ClientException(e);
        }
    }

    public static CommandResponse receiveResponse(InputStream inputStream) {
        try {
            int version = inputStream.read();
            if (version != 0x05) throw new SOCKS5ClientException("Invalid version");
            REP rep = REP.valueOf(inputStream.read());
            int rsv = inputStream.read();
            if (rsv != 0x00) throw new SOCKS5ClientException("Invalid rsv");
            ATYP atyp = ATYP.valueOf(inputStream.read());
            byte[] bndAddrBytes = switch (atyp) {
                case IP_V4_ADDRESS -> inputStream.readNBytes(4);
                case DOMAINNAME -> {
                    int length = inputStream.read();
                    yield inputStream.readNBytes(length);
                }
                case IP_V6_ADDRESS -> inputStream.readNBytes(16);
            };
            int bndPort = ((inputStream.read() & 0xff) << 8) | (inputStream.read() & 0xff);
            InetSocketAddress inetSocketAddress = switch (atyp) {
                case DOMAINNAME -> new InetSocketAddress(new String(bndAddrBytes), bndPort);
                case IP_V4_ADDRESS, IP_V6_ADDRESS -> new InetSocketAddress(InetAddress.getByAddress(bndAddrBytes), bndPort);
            };
            return new CommandResponse(rep, atyp, inetSocketAddress);
        } catch (Exception e) {
            throw new SOCKS5ClientException(e);
        }
    }
}
