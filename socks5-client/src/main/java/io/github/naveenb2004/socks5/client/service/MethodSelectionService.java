package io.github.naveenb2004.socks5.client.service;

import io.github.naveenb2004.socks5.base.exception.SOCKS5ServiceException;
import io.github.naveenb2004.socks5.base.method.SOCKS5Method;
import io.github.naveenb2004.socks5.client.exception.SOCKS5ClientMethodException;
import io.github.naveenb2004.socks5.client.exception.SOCKS5ClientServiceException;
import io.github.naveenb2004.socks5.client.exception.SOCKS5ClientVersionException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Map;

public final class MethodSelectionService {
    private final Socket socket;
    private final Map<Byte, SOCKS5Method> socks5Methods;

    private InputStream inputStream;
    private OutputStream outputStream;
    private SOCKS5Method socks5Method;

    public MethodSelectionService(Socket socket,
                                  Map<Byte, SOCKS5Method> socks5Methods) {
        this.socket = socket;
        this.socks5Methods = socks5Methods;
    }

    public void init() throws SOCKS5ClientServiceException {
        try {
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();

            processMethodSelection();
            processMethodSubNegotiation();
        } catch (Exception e) {
            throw new SOCKS5ClientServiceException(e);
        }
    }

    private void processMethodSelection() throws IOException {
        outputStream.write(0x05);
        outputStream.write(socks5Methods.size());
        for (Byte methodId : socks5Methods.keySet()) outputStream.write(methodId);
        outputStream.flush();

        int version = inputStream.read();
        if (version != 0x05) throw new SOCKS5ClientVersionException("Invalid SOCKS version from server");
        int selectedMethod = inputStream.read();
        if (selectedMethod == 0xff) throw new SOCKS5ClientMethodException("No acceptable SOCKS5 method");
        socks5Method = socks5Methods.get((byte) selectedMethod);
    }

    private void processMethodSubNegotiation() throws SOCKS5ServiceException {
        socks5Method.negotiate(inputStream, outputStream);
        socks5Method.setupDecapsulation(inputStream);
        socks5Method.setupEncapsulation(outputStream);
    }
}
