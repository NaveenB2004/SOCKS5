package io.github.naveenb2004.socks5.client.command.processor;

import io.github.naveenb2004.socks5.base.ATYP;
import io.github.naveenb2004.socks5.base.CMD;
import io.github.naveenb2004.socks5.client.command.SOCKS5Response;
import io.github.naveenb2004.socks5.client.exception.SOCKS5ClientException;

import java.net.Socket;

public final class ConnectProcessor extends CommandProcessor {
    public ConnectProcessor(Socket socks5Server,
                            ATYP atyp,
                            byte[] dstAddr,
                            byte[] dstPort) {
        super(socks5Server, CMD.CONNECT, atyp, dstAddr, dstPort);
    }

    @Override
    public SOCKS5Response process() {
        try {
            super.sendRequest();

            return null;
        } catch (Exception e) {
            throw new SOCKS5ClientException(e);
        }
    }

}
