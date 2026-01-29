package io.github.naveenb2004.socks5.server.service;

import io.github.naveenb2004.socks5.base.exception.SOCKS5ServiceException;
import io.github.naveenb2004.socks5.base.method.SOCKS5Method;
import io.github.naveenb2004.socks5.server.exception.SOCKS5ServerMethodException;
import io.github.naveenb2004.socks5.server.exception.SOCKS5ServerServiceException;
import io.github.naveenb2004.socks5.server.exception.SOCKS5ServerVersionException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class MethodSelectionService {
    private final Socket socket;
    private final List<SOCKS5Method> socks5Methods;

    private InputStream inputStream;
    private OutputStream outputStream;
    private SOCKS5Method socks5Method;

    public MethodSelectionService(Socket socket,
                                  List<SOCKS5Method> socks5Methods) {
        this.socket = socket;
        this.socks5Methods = socks5Methods;
    }

    public void init() throws SOCKS5ServerServiceException {
        try {
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();

            processMethodSelection();
            processMethodSubNegotiation();
        } catch (Exception e) {
            throw new SOCKS5ServerServiceException(e);
        }
    }

    private void processMethodSelection() throws IOException {
        int version = inputStream.read();
        if (version != 0x05) throw new SOCKS5ServerVersionException("Invalid SOCKS version from client");
        int nmethods = inputStream.read();
        Set<Byte> methods = new HashSet<>(nmethods);
        for (int i = 0; i < nmethods; i++) {
            methods.add((byte) inputStream.read());
        }
        for (SOCKS5Method method : socks5Methods) {
            if (methods.contains(method.getMethodId())) {
                socks5Method = method;
                break;
            }
        }
        if (socks5Method == null) {
            outputStream.write(0x05);
            outputStream.write(0xff);
            outputStream.flush();
            socket.close();
            throw new SOCKS5ServerMethodException("No acceptable SOCKS5 method");
        }
    }

    private void processMethodSubNegotiation() throws SOCKS5ServiceException {
        socks5Method.negotiate(inputStream, outputStream);
        socks5Method.setupDecapsulation(inputStream);
        socks5Method.setupEncapsulation(outputStream);
    }
}
