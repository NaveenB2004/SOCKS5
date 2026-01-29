package io.github.naveenb2004.socks5.client.service;

import io.github.naveenb2004.socks5.client.exception.SOCKS5ClientException;
import io.github.naveenb2004.socks5.client.method.SOCKS5ClientMethod;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Map;

public final class MethodSelectionService {
    private final Socket socket;
    private final Map<Byte, SOCKS5ClientMethod> socks5Methods;

    private InputStream inputStream;
    private OutputStream outputStream;
    private SOCKS5ClientMethod socks5ClientMethod;

    public MethodSelectionService(Socket socket,
                                  Map<Byte, SOCKS5ClientMethod> socks5Methods) {
        this.socket = socket;
        this.socks5Methods = socks5Methods;
    }

    public void init() {
        try {
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();

            processMethodSelection();
            processMethodSubNegotiation();
        } catch (Exception e) {
            throw new SOCKS5ClientException(e);
        }
    }

    private void processMethodSelection() throws IOException {
        outputStream.write(0x05);
        outputStream.write(socks5Methods.size());
        for (Byte methodId : socks5Methods.keySet()) outputStream.write(methodId);
        outputStream.flush();

        int version = inputStream.read();
        if (version != 0x05) throw new SOCKS5ClientException("Invalid SOCKS version from server");
        int selectedMethod = inputStream.read();
        if (selectedMethod == 0xff) throw new SOCKS5ClientException("No acceptable SOCKS5 method");
        socks5ClientMethod = socks5Methods.get((byte) selectedMethod);
    }

    private void processMethodSubNegotiation() {
        socks5ClientMethod.negotiateAsClient(inputStream, outputStream);
        socks5ClientMethod.setupDecapsulationAsClient(inputStream);
        socks5ClientMethod.setupEncapsulationAsClient(outputStream);
    }
}
