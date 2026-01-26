package io.github.naveenb2004.socks5.server.service;

import io.github.naveenb2004.socks5.server.config.SOCKS5ServerConfiguration;
import io.github.naveenb2004.socks5.server.auth.SOCKS5ServerAuth;
import io.github.naveenb2004.socks5.server.exception.SOCKS5ServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Set;
import java.util.TreeSet;

public final class ProtocolHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProtocolHandler.class);

    private final Socket socket;
    private final SOCKS5ServerConfiguration configuration;

    private InputStream inputStream;
    private OutputStream outputStream;

    public ProtocolHandler(Socket socket,
                           SOCKS5ServerConfiguration configuration) {
        this.socket = socket;
        this.configuration = configuration;
    }

    public boolean handle() {
        try {
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            if (!methodSelectionPhase()) return false;
            if (!clientRequestPhase()) return false;
        } catch (IOException e) {
            throw new SOCKS5ServerException(e);
        }
        return false;
    }

    private boolean verifyVersion() throws IOException {
        int ver = inputStream.read();
        if (ver != 0x05) {
            socket.close();
            return false;
        }
        return true;
    }

    private boolean methodSelectionPhase() throws IOException {
        if (!verifyVersion()) return false;

        int nmethods = inputStream.read();
        Set<Byte> receivedMethods = new TreeSet<>();
        for (int i = 0; i < nmethods; i++) {
            receivedMethods.add((byte) inputStream.read());
        }

        SOCKS5ServerAuth acceptedMethod = null;
        for (SOCKS5ServerAuth auth : configuration.getSocks5ServerAuths()) {
            if (receivedMethods.contains(auth.getMethodId())) {
                acceptedMethod = auth;
                break;
            }
        }

        outputStream.write(0x05);
        if (acceptedMethod == null) {
            outputStream.write(0xff);
            outputStream.flush();
            socket.close();
            return false;
        }

        outputStream.write(acceptedMethod.getMethodId());
        outputStream.flush();

        // method-dependent sub-negotiation
        acceptedMethod.authenticateAsServer(inputStream, outputStream);
        // method-dependent encapsulation
        inputStream = acceptedMethod.getDecapsulationServerInputStream(inputStream);
        outputStream = acceptedMethod.getEncapsulationServerOutputStream(outputStream);
        return true;
    }

    private boolean clientRequestPhase() throws IOException {
        if (!verifyVersion()) return false;

        int cmd, rsv, atyp;
        byte[] dstAddr, dstPort;

        cmd = inputStream.read();
        rsv = inputStream.read();
        atyp = inputStream.read();
        switch (atyp) {
            case 0x01 -> dstAddr = new byte[4];
            case 0x03 -> dstAddr = new byte[inputStream.read()];
            case 0x04 -> dstAddr = new byte[16];
            default -> {
                socket.close();
                return false;
            }
        }
        int c = inputStream.read(dstAddr);
        if (c != dstAddr.length) {
            socket.close();
            return false;
        }

        dstPort = new byte[2];
        c = inputStream.read(dstPort);
        if (c != dstPort.length) {
            socket.close();
            return false;
        }

//        try {
//
//        }

        return true;
    }
}
