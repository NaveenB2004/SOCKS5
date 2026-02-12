package io.github.naveenb2004.socks5.server.method.service;

import io.github.naveenb2004.socks5.server.exception.SOCKS5ServerException;
import io.github.naveenb2004.socks5.server.method.SOCKS5ServerMethod;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class MethodSelectionService {
    private final Socket socket;
    private final List<SOCKS5ServerMethod> socks5ServerMethods;

    private InputStream inputStream;
    private OutputStream outputStream;
    private SOCKS5ServerMethod socks5ServerMethod;

    public MethodSelectionService(Socket socket,
                                  List<SOCKS5ServerMethod> socks5ServerMethods) {
        this.socket = socket;
        this.socks5ServerMethods = socks5ServerMethods;
    }

    public void init() {
        try {
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();

            processMethodSelection();
            processMethodSubNegotiation();
        } catch (Exception e) {
            throw new SOCKS5ServerException(e);
        }
    }

    private void processMethodSelection() throws IOException {
        int version = inputStream.read();
        if (version != 0x05) throw new SOCKS5ServerException("Invalid SOCKS version from client");
        int nmethods = inputStream.read();
        Set<Byte> methods = new HashSet<>(nmethods);
        for (int i = 0; i < nmethods; i++) {
            methods.add((byte) inputStream.read());
        }
        for (SOCKS5ServerMethod method : socks5ServerMethods) {
            if (methods.contains(method.getMethodId())) {
                socks5ServerMethod = method;
                break;
            }
        }
        if (socks5ServerMethod == null) {
            outputStream.write(0x05);
            outputStream.write(0xff);
            outputStream.flush();
            socket.close();
            throw new SOCKS5ServerException("No acceptable SOCKS5 method");
        }
        outputStream.write(0x05);
        outputStream.write(socks5ServerMethod.getMethodId());
    }

    private void processMethodSubNegotiation() {
        socks5ServerMethod.negotiateAsServer(inputStream, outputStream);
        socks5ServerMethod.setupDecapsulationAsServer(inputStream);
        socks5ServerMethod.setupEncapsulationAsServer(outputStream);
    }
}
