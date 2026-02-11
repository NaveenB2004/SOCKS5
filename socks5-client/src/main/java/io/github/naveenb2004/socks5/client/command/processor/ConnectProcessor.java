package io.github.naveenb2004.socks5.client.command.processor;

import io.github.naveenb2004.socks5.base.ATYP;
import io.github.naveenb2004.socks5.base.CMD;
import io.github.naveenb2004.socks5.client.command.ConnectResponse;
import io.github.naveenb2004.socks5.client.command.SOCKS5Response;
import io.github.naveenb2004.socks5.client.exception.SOCKS5ClientException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public final class ConnectProcessor extends CommandProcessor {
    private final ConnectResponse response;

    public ConnectProcessor(Socket socks5Server,
                            ATYP atyp,
                            byte[] dstAddr,
                            byte[] dstPort) throws IOException {
        var response = new ConnectResponse(socks5Server);
        super(socks5Server, CMD.CONNECT, atyp, dstAddr, dstPort, response);
        this.response = response;
    }

    @Override
    public SOCKS5Response process() {
        try {
            super.sendRequest();
            super.consumeResponse();

            return response;
        } catch (Exception e) {
            throw new SOCKS5ClientException(e);
        }
    }
}
