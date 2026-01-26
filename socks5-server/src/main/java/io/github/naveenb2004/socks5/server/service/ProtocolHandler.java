package io.github.naveenb2004.socks5.server.service;

import io.github.naveenb2004.socks5.base.CMD;
import io.github.naveenb2004.socks5.server.config.SOCKS5ServerConfiguration;
import io.github.naveenb2004.socks5.server.auth.SOCKS5ServerAuth;
import io.github.naveenb2004.socks5.server.SOCKS5ServerException;
import io.github.naveenb2004.socks5.base.util.ReqRsp;
import io.github.naveenb2004.socks5.server.service.reqHandler.ConnectReqHandler;
import io.github.naveenb2004.socks5.server.service.util.RulesetEnforcer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Set;
import java.util.TreeSet;

public final class ProtocolHandler implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProtocolHandler.class);

    private final Socket socket;
    private final SOCKS5ServerConfiguration configuration;

    private InputStream inputStream;
    private OutputStream outputStream;
    private ReqRsp clientRequest;

    public ProtocolHandler(Socket socket,
                           SOCKS5ServerConfiguration configuration) {
        this.socket = socket;
        this.configuration = configuration;
    }

    @Override
    public void run() {
        try {
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            methodSelectionPhase();
            clientRequestPhase();
            clientRequestInitPhase();
        } catch (Exception e) {
            try {
                if (!socket.isClosed()) socket.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            throw new SOCKS5ServerException(e);
        }
    }

    private void verifySocksVersion() throws IOException {
        int ver = inputStream.read();
        if (ver != 0x05) {
            socket.close();
            throw new ServerServiceException("SOCKS version mismatch");
        }
    }

    private void methodSelectionPhase() throws IOException {
        verifySocksVersion();

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
            throw new ServerServiceException("No acceptable auth method found");
        }

        outputStream.write(acceptedMethod.getMethodId());
        outputStream.flush();

        // method-dependent sub-negotiation
        acceptedMethod.authenticateAsServer(inputStream, outputStream);
        // method-dependent encapsulation/decapsulation
        inputStream = acceptedMethod.getDecapsulationServerInputStream(inputStream);
        outputStream = acceptedMethod.getEncapsulationServerOutputStream(outputStream);
    }

    private void clientRequestPhase() throws IOException {
        verifySocksVersion();

        int cmd, rsv, atyp;
        byte[] dstAddr, dstPort;
        ReqRsp.ClientRequestBuilder clientRequestBuilder = ReqRsp.builder();

        cmd = inputStream.read();
        clientRequestBuilder.cmd(cmd);

        rsv = inputStream.read();
        if (rsv != 0x00) {
            socket.close();
            throw new ServerServiceException("Invalid RSV");
        }

        atyp = inputStream.read();
        clientRequestBuilder.atyp(atyp);

        switch (atyp) {
            case 0x01 -> dstAddr = new byte[4];
            case 0x03 -> dstAddr = new byte[inputStream.read()];
            case 0x04 -> dstAddr = new byte[16];
            default -> throw new IllegalStateException("Unexpected value: " + atyp);
        }
        int c = inputStream.read(dstAddr);
        if (c != dstAddr.length) {
            socket.close();
            throw new ServerServiceException("Invalid DST.ADDR");
        }
        clientRequestBuilder.addr(dstAddr);

        dstPort = new byte[2];
        c = inputStream.read(dstPort);
        if (c != dstPort.length) {
            socket.close();
            throw new ServerServiceException("Invalid DST.PORT");
        }
        clientRequestBuilder.port(dstPort);
        clientRequest = clientRequestBuilder.build();

        if (configuration.getSocks5ServerRuleset() != null) {
            RulesetEnforcer.builder()
                    .ruleset(configuration.getSocks5ServerRuleset())
                    .outputStream(outputStream)
                    .clientRequest(clientRequest)
                    .build().enforce();
        }
    }

    private void clientRequestInitPhase() {
        switch ((CMD) clientRequest.getReqRspField()) {
            case CONNECT -> {
                ConnectReqHandler connectReqHandler;
                try {
                    connectReqHandler = new ConnectReqHandler(
                            socket,
                            clientRequest.getAddr(),
                            clientRequest.getPort(),
                            configuration);
                    connectReqHandler.handle();
                } catch (Exception e) {

                }
            }
            case BIND -> {}
            case UDP_ASSOCIATE -> {}
        }
    }
}
