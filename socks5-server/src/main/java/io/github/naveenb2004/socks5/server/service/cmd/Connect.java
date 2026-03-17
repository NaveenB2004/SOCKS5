/*
 * Copyright (c) 2026 Naveen N. Balasooriya
 *
 * This source code is licensed under the MIT License.
 * See the LICENSE file in the project root for full license information.
 */

package io.github.naveenb2004.socks5.server.service.cmd;

import io.github.naveenb2004.socks5.base.template.CmdRequest;
import io.github.naveenb2004.socks5.server.configuration.SOCKS5ServerConfig;
import io.github.naveenb2004.socks5.server.exception.SOCKS5ServerException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public final class Connect implements CmdHandler {
    private final Socket clientSocket;
    private final CmdRequest clientRequest;
    private final SOCKS5ServerConfig serverConfig;

    public Connect(final Socket clientSocket,
                   final CmdRequest clientRequest,
                   final SOCKS5ServerConfig serverConfig) throws IOException {
        this.clientSocket = clientSocket;
        this.clientRequest = clientRequest;
        this.serverConfig = serverConfig;
    }

    @Override
    public void handle() throws IOException {
        final var destination = CmdHandler.getDestination(clientRequest, clientSocket.getOutputStream());
        try (final var destSocket = CmdHandler.buildSocket(destination)) {
            serverConfig.getConcurrentThreadFactory().newThread(() -> {
                try {
                    wireIO(clientSocket.getInputStream(), destSocket.getOutputStream());
                    destSocket.shutdownOutput();
                } catch (IOException e) {
                    throw new SOCKS5ServerException(e);
                }
            }).start();

            wireIO(destSocket.getInputStream(), clientSocket.getOutputStream());
            clientSocket.shutdownOutput();
        }
    }

    private void wireIO(final InputStream inputStream,
                        final OutputStream outputStream) throws IOException {
        int byteCount;
        byte[] buffer = new byte[serverConfig.getInternalBufferSize()];
        while ((byteCount = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, byteCount);
        }
    }
}
