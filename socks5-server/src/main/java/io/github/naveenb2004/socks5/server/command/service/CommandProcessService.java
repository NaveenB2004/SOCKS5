package io.github.naveenb2004.socks5.server.command.service;

import io.github.naveenb2004.socks5.base.ATYP;
import io.github.naveenb2004.socks5.base.CMD;
import io.github.naveenb2004.socks5.base.REP;
import io.github.naveenb2004.socks5.server.command.processor.CommandProcessor;
import io.github.naveenb2004.socks5.server.command.processor.ConnectProcessor;
import io.github.naveenb2004.socks5.server.config.SOCKS5Ruleset;
import io.github.naveenb2004.socks5.server.exception.SOCKS5ServerException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;

public final class CommandProcessService {
    private final Socket clientSocket;
    private final SOCKS5Ruleset socks5Ruleset;

    private InputStream inputStream;
    private OutputStream outputStream;

    private CMD command;
    private ATYP addressType;
    private InetSocketAddress destination;
    private CommandProcessor commandProcessor;

    public CommandProcessService(Socket clientSocket,
                                 SOCKS5Ruleset socks5Ruleset) {
        this.clientSocket = clientSocket;
        this.socks5Ruleset = socks5Ruleset;
    }

    public void init() {
        try {
            inputStream = clientSocket.getInputStream();
            outputStream = clientSocket.getOutputStream();

            consumeRequest();
            enforceRules();
            redirectToProcessor();
        } catch (Exception e) {
            throw new SOCKS5ServerException(e);
        }
    }

    private void consumeRequest() throws IOException {
        int version = inputStream.read();
        if (version != 0x05) throw new SOCKS5ServerException("Invalid version");
        command = CMD.valueOf(inputStream.read());
        int rsv = inputStream.read();
        if (rsv != 0x00) throw new SOCKS5ServerException("Invalid rsv");
        addressType = ATYP.valueOf(inputStream.read());
        switch (addressType) {
            case IP_V4_ADDRESS -> {
                InetAddress addr = Inet4Address.getByAddress(inputStream.readNBytes(4));
                int port = (inputStream.read() & 0xFF) << 8 | (inputStream.read() & 0xFF);
                destination = new InetSocketAddress(addr, port);
            }
            case DOMAINNAME -> {
                int len = inputStream.read();
                String addr = new String(inputStream.readNBytes(len));
                int port = (inputStream.read() & 0xFF) << 8 | (inputStream.read() & 0xFF);
                destination = new InetSocketAddress(addr, port);
            }
            case IP_V6_ADDRESS -> {
                InetAddress addr = Inet6Address.getByAddress(inputStream.readNBytes(16));
                int port = (inputStream.read() & 0xFF) << 8 | (inputStream.read() & 0xFF);
                destination = new InetSocketAddress(addr, port);
            }
        }
    }

    private void enforceRules() throws IOException {
        switch (socks5Ruleset.getCommandsState()) {
            case ALLOW -> {
                if (!socks5Ruleset.getCommands().contains(command)) replyOnRuleFailer();
            }
            case DENY -> {
                if (socks5Ruleset.getCommands().contains(command)) replyOnRuleFailer();
            }
        }

        switch (socks5Ruleset.getAddressTypesState()) {
            case ALLOW -> {
                if (!socks5Ruleset.getAddressTypes().contains(addressType)) replyOnRuleFailer();
            }
            case DENY -> {
                if (socks5Ruleset.getAddressTypes().contains(addressType)) replyOnRuleFailer();
            }
        }

        byte[] addrBytes = switch (addressType) {
            case IP_V4_ADDRESS, IP_V6_ADDRESS -> destination.getAddress().getAddress();
            case DOMAINNAME -> destination.getHostName().getBytes();
        };
        switch (socks5Ruleset.getDestAddressesState()) {
            case ALLOW -> {
                if (!socks5Ruleset.getDestAddresses().contains(addrBytes)) replyOnRuleFailer();
            }
            case DENY -> {
                if (socks5Ruleset.getDestAddresses().contains(addrBytes)) replyOnRuleFailer();
            }
        }

        switch (socks5Ruleset.getDestPortsState()) {
            case ALLOW -> {
                if (!socks5Ruleset.getDestPorts().contains(destination.getPort())) replyOnRuleFailer();
            }
            case DENY -> {
                if (socks5Ruleset.getDestPorts().contains(destination.getPort())) replyOnRuleFailer();
            }
        }
    }

    private void redirectToProcessor() {
        commandProcessor = switch (command) {
            case CONNECT -> new ConnectProcessor();
            case BIND -> null;
            case UDP_ASSOCIATE -> null;
        };
        commandProcessor.process();
    }

    private void replyOnRuleFailer() throws IOException {
        CommandProcessor.sendResponse(outputStream, REP.CONNECTION_NOT_ALLOWED_BY_RULESET,
                ATYP.IP_V4_ADDRESS, InetAddress.getByAddress(new byte[]{0, 0, 0, 0}), 0);
        throw new SOCKS5ServerException("SOCKS5 rules violation");
    }
}
