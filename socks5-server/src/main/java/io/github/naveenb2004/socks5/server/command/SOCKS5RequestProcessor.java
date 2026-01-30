package io.github.naveenb2004.socks5.server.command;

import io.github.naveenb2004.socks5.base.ATYP;
import io.github.naveenb2004.socks5.base.command.CMD;
import io.github.naveenb2004.socks5.base.command.CommandRequest;
import io.github.naveenb2004.socks5.base.command.CommandResponse;
import io.github.naveenb2004.socks5.server.config.SOCKS5Ruleset;
import io.github.naveenb2004.socks5.server.exception.SOCKS5ServerException;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

public abstract sealed class SOCKS5RequestProcessor
        permits BindRequestProcessor, ConnectRequestProcessor, UdpAssociateRequestProcessor {
    protected final CMD command;
    protected final Socket clientSocket;
    protected final SOCKS5Ruleset socks5Ruleset;

    public SOCKS5RequestProcessor(CMD command,
                                  Socket clientSocket,
                                  SOCKS5Ruleset socks5Ruleset) {
        this.command = command;
        this.clientSocket = clientSocket;
        this.socks5Ruleset = socks5Ruleset;
    }

    public static void sendResponse(OutputStream outputStream,
                                    CommandResponse commandResponse) {
        try {
            outputStream.write(0x05);
            outputStream.write(commandResponse.rep().getValue());
            outputStream.write(0x00);
            outputStream.write(commandResponse.atyp().getValue());
            if (commandResponse.atyp() == ATYP.DOMAINNAME) {
                outputStream.write(commandResponse.dest().getHostName().length());
                outputStream.write(commandResponse.dest().getHostName().getBytes());
            } else {
                outputStream.write(commandResponse.dest().getAddress().getAddress());
            }
            outputStream.write((commandResponse.dest().getPort() >>> 8) & 0xff);
            outputStream.write(commandResponse.dest().getPort() & 0xff);
        } catch (Exception e) {
            throw new SOCKS5ServerException(e);
        }
    }

    public static CommandRequest receiveRequest(InputStream inputStream) {
        try {
            int version = inputStream.read();
            if (version != 0x05) throw new SOCKS5ServerException("Invalid version");
            CMD cmd = CMD.valueOf(inputStream.read());
            int rsv = inputStream.read();
            if (rsv != 0x00) throw new SOCKS5ServerException("Invalid rsv");
            ATYP atyp = ATYP.valueOf(inputStream.read());
            byte[] dstAddrBytes = switch (atyp) {
                case IP_V4_ADDRESS -> inputStream.readNBytes(4);
                case DOMAINNAME -> {
                    int length = inputStream.read();
                    yield inputStream.readNBytes(length);
                }
                case IP_V6_ADDRESS -> inputStream.readNBytes(16);
            };
            int dstPort = ((inputStream.read() & 0xff) << 8) | (inputStream.read() & 0xff);
            InetSocketAddress inetSocketAddress = switch (atyp) {
                case DOMAINNAME -> new InetSocketAddress(new String(dstAddrBytes), dstPort);
                case IP_V4_ADDRESS, IP_V6_ADDRESS -> new InetSocketAddress(InetAddress.getByAddress(dstAddrBytes), dstPort);
            };
            return new CommandRequest(cmd, atyp, inetSocketAddress);
        } catch (Exception e) {
            throw new SOCKS5ServerException(e);
        }
    }
}
