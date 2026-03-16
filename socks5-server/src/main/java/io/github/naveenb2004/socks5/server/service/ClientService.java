/*
 * Copyright (c) 2026 Naveen N. Balasooriya
 *
 * This source code is licensed under the MIT License.
 * See the LICENSE file in the project root for full license information.
 */

package io.github.naveenb2004.socks5.server.service;

import io.github.naveenb2004.socks5.base.AddressType;
import io.github.naveenb2004.socks5.base.Reply;
import io.github.naveenb2004.socks5.base.template.AuthRequest;
import io.github.naveenb2004.socks5.base.template.AuthResponse;
import io.github.naveenb2004.socks5.base.template.CmdRequest;
import io.github.naveenb2004.socks5.base.template.CmdResponse;
import io.github.naveenb2004.socks5.server.authentication.AbstractServerAuth;
import io.github.naveenb2004.socks5.server.configuration.SOCKS5ServerConfig;
import io.github.naveenb2004.socks5.server.configuration.SOCKS5ServerRuleset;
import io.github.naveenb2004.socks5.server.exception.SOCKS5ServerException;
import io.github.naveenb2004.socks5.server.service.cmd.Bind;
import io.github.naveenb2004.socks5.server.service.cmd.CmdHandler;
import io.github.naveenb2004.socks5.server.service.cmd.Connect;
import io.github.naveenb2004.socks5.server.service.cmd.UdpAssociate;
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
            authSelection.authenticate(clientInputStream, clientOutputStream);

            CmdRequest commandSelection = commandSelect();
            CmdHandler cmdHandler = switch (commandSelection.command()) {
                case CONNECT -> new Connect(clientSocket, commandSelection, serverConfig);
                case BIND -> new Bind(clientSocket, commandSelection, serverConfig);
                case UDP_ASSOCIATE -> new UdpAssociate(clientSocket, commandSelection, serverConfig);
            };
            cmdHandler.handle();
        } catch (IOException e) {
            throw new SOCKS5ServerException(e);
        } finally {
            try {
                if (!clientSocket.isClosed()) clientSocket.close();
            } catch (IOException e) {
                LOGGER.atError().log(e.getMessage());
            }
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
        if (matchedAuth == null) throw new SOCKS5ServerException("No matching auth method found");
        return matchedAuth;
    }

    private CmdRequest commandSelect() throws IOException {
        final CmdRequest cmdRequest = ServerCmdService.receiveCmdReq(clientInputStream);
        final SOCKS5ServerRuleset ruleset = serverConfig.getServerRuleset();

        if (ruleset == null) return cmdRequest;
        if (ruleset.getEnforceCommands() != null) {
            if (ruleset.getEnforceCommands() && !ruleset.getCommands().contains(cmdRequest.command())) {
                sendCmdRuleFailResponse();
                throw new SOCKS5ServerException("Command not whitelisted by the ruleset");
            }
            if (!ruleset.getEnforceCommands() && ruleset.getCommands().contains(cmdRequest.command())) {
                sendCmdRuleFailResponse();
                throw new SOCKS5ServerException("Command blacklisted by the ruleset");
            }
        }
        if (ruleset.getEnforceAddressTypes() != null) {
            if (ruleset.getEnforceAddressTypes() && !ruleset.getAddressTypes().contains(cmdRequest.addressType())) {
                sendCmdRuleFailResponse();
                throw new SOCKS5ServerException("Address type not whitelisted by the ruleset");
            }
            if (!ruleset.getEnforceAddressTypes() && ruleset.getAddressTypes().contains(cmdRequest.addressType())) {
                sendCmdRuleFailResponse();
                throw new SOCKS5ServerException("Address type blacklisted by the ruleset");
            }
        }
        if (ruleset.getEnforceAddresses() != null) {
            if (ruleset.getEnforceAddresses() && !ruleset.getAddresses().contains(cmdRequest.destAddress())) {
                sendCmdRuleFailResponse();
                throw new SOCKS5ServerException("Address not whitelisted by the ruleset");
            }
            if (!ruleset.getEnforceAddresses() && ruleset.getAddresses().contains(cmdRequest.destAddress())) {
                sendCmdRuleFailResponse();
                throw new SOCKS5ServerException("Address blacklisted by the ruleset");
            }
        }
        if (ruleset.getEnforcePorts() != null) {
            if (ruleset.getEnforcePorts() && !ruleset.getPorts().contains(cmdRequest.destPort())) {
                sendCmdRuleFailResponse();
                throw new SOCKS5ServerException("Port not whitelisted by the ruleset");
            }
            if (!ruleset.getEnforcePorts() && ruleset.getPorts().contains(cmdRequest.destPort())) {
                sendCmdRuleFailResponse();
                throw new SOCKS5ServerException("Port blacklisted by the ruleset");
            }
        }
        if (ruleset.getEnforceDestinations() != null) {
            if (ruleset.getEnforceDestinations() && (ruleset.getDestinations().get(cmdRequest.destAddress()) != cmdRequest.destPort())) {
                sendCmdRuleFailResponse();
                throw new SOCKS5ServerException("Destination not whitelisted by the ruleset");
            }
            if (!ruleset.getEnforceDestinations() && (ruleset.getDestinations().get(cmdRequest.destAddress()) == cmdRequest.destPort())) {
                sendCmdRuleFailResponse();
                throw new SOCKS5ServerException("Destination blacklisted by the ruleset");
            }
        }

        return cmdRequest;
    }

    private void sendCmdRuleFailResponse() throws IOException {
        var cmdResponse = new CmdResponse(Reply.CONNECTION_NOT_ALLOWED_BY_RULESET, AddressType.DOMAIN_NAME, new byte[]{0x00}, 0);
        ServerCmdService.sendCmdResp(clientOutputStream, cmdResponse);
    }
}
