/*
 * Copyright (c) 2026 Naveen N. Balasooriya
 *
 * This source code is licensed under the MIT License.
 * See the LICENSE file in the project root for full license information.
 */

package io.github.naveenb2004.socks5.server.service;

import io.github.naveenb2004.socks5.base.Command;
import io.github.naveenb2004.socks5.base.template.AuthRequest;
import io.github.naveenb2004.socks5.base.template.AuthResponse;
import io.github.naveenb2004.socks5.server.authentication.AbstractServerAuth;
import io.github.naveenb2004.socks5.server.configuration.SOCKS5ServerConfig;
import io.github.naveenb2004.socks5.server.exception.SOCKS5ServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Map;

public final class ClientService implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientService.class);

    private final Socket clientSocket;
    private final InputStream clientInputStream;
    private final OutputStream clientOutputStream;
    private final SOCKS5ServerConfig serverConfig;

    public ClientService(final Socket clientSocket,
                         final SOCKS5ServerConfig serverConfig) throws IOException {
        this.clientSocket = clientSocket;
        this.clientInputStream = clientSocket.getInputStream();
        this.clientOutputStream = clientSocket.getOutputStream();
        this.serverConfig = serverConfig;
    }

    @Override
    public void run() {
        try {
            AbstractServerAuth authSelection = authSelect();
            if (authSelection == null) {
                LOGGER.atDebug().log("No acceptable authentication methods from client; disconnecting...");
                return;
            }
            authSelection.authenticate(clientInputStream, clientOutputStream);

            Command commandSelection = commandSelect();
        } catch (IOException e) {
            throw new SOCKS5ServerException(e);
        } finally {
            try {
                if (!clientSocket.isClosed()) clientSocket.close();
            } catch (IOException _) {}
        }
    }

    private AbstractServerAuth authSelect() throws IOException {
        Map<Integer, AbstractServerAuth> serverAuths = serverConfig.getServerAuths();
        AuthRequest authRequest = ServerAuthService.receiveAuthSelectionRequest(clientInputStream);
        AbstractServerAuth matchedAuth = null;

        for (int clientAuthId : authRequest.authMethodIds()) {
            matchedAuth = serverAuths.get(clientAuthId);
            if (matchedAuth != null) break;
        }

        AuthResponse authResponse = new AuthResponse(matchedAuth == null ? 0xff : matchedAuth.getAuthMethodId());
        ServerAuthService.sendAuthSelectionResponse(clientOutputStream, authResponse);
        return matchedAuth;
    }

    private Command commandSelect() throws IOException {

    }
}
