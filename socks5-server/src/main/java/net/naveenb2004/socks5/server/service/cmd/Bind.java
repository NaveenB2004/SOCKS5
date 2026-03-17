/*
 * Copyright (c) 2026 Naveen N. Balasooriya
 *
 * This source code is licensed under the MIT License.
 * See the LICENSE file in the project root for full license information.
 */

package net.naveenb2004.socks5.server.service.cmd;

import net.naveenb2004.socks5.base.template.CmdRequest;
import net.naveenb2004.socks5.server.configuration.SOCKS5ServerConfig;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public final class Bind implements CmdHandler {
    private final InputStream clientInputStream;
    private final OutputStream clientOutputStream;
    private final CmdRequest clientRequest;
    private final SOCKS5ServerConfig serverConfig;

    public Bind(final Socket clientSocket,
                final CmdRequest clientRequest,
                final SOCKS5ServerConfig serverConfig) throws IOException {
        this.clientInputStream = clientSocket.getInputStream();
        this.clientOutputStream = clientSocket.getOutputStream();
        this.clientRequest = clientRequest;
        this.serverConfig = serverConfig;
    }

    @Override
    public void handle() {

    }
}
