package io.github.naveenb2004.socks5.server.service.util;

import io.github.naveenb2004.socks5.base.CMD;
import io.github.naveenb2004.socks5.base.REP;
import io.github.naveenb2004.socks5.base.util.ReqRsp;
import io.github.naveenb2004.socks5.server.config.SOCKS5ServerRuleset;
import io.github.naveenb2004.socks5.server.service.ServerServiceException;

import java.io.IOException;
import java.io.OutputStream;

public final class RulesetEnforcer {
    private final SOCKS5ServerRuleset ruleset;
    private final OutputStream outputStream;
    private final ReqRsp reqRsp;

    private RulesetEnforcer(SOCKS5ServerRuleset ruleset,
                            OutputStream outputStream,
                            ReqRsp reqRsp) {
        this.ruleset = ruleset;
        this.outputStream = outputStream;
        this.reqRsp = reqRsp;
    }

    public void enforce() throws IOException, ServerServiceException {
        if (ruleset.isFilterCommands() && !ruleset.getFilterCommandTargets().contains((CMD) reqRsp.getReqRspField())) {
            respondOnError(REP.COMMAND_NOT_SUPPORTED);
        }
        if (ruleset.isFilterAddressTypes() && !ruleset.getFilterAddressTypeTargets().contains(reqRsp.getAtyp())) {
            respondOnError(REP.ADDRESS_TYPE_NOT_SUPPORTED);
        }
        if (ruleset.getFilterHosts() != null) {
            if (ruleset.isFilterHostsAllowed() && !ruleset.getFilterHostTargets().contains(reqRsp.getAddr())) {
                respondOnError(REP.CONNECTION_NOT_ALLOWED_BY_RULESET);
            } else if (ruleset.isFilterHostsDenied() && ruleset.getFilterHostTargets().contains(reqRsp.getAddr())) {
                respondOnError(REP.CONNECTION_NOT_ALLOWED_BY_RULESET);
            }
        }
        if (ruleset.getFilterPorts() != null) {
            if (ruleset.isFilterPortsAllowed() && !ruleset.getFilterPortTargets().contains(reqRsp.getPort())) {
                respondOnError(REP.CONNECTION_NOT_ALLOWED_BY_RULESET);
            } else if (ruleset.isFilterPortsDenied() && ruleset.getFilterPortTargets().contains(reqRsp.getPort())) {
                respondOnError(REP.CONNECTION_NOT_ALLOWED_BY_RULESET);
            }
        }
    }

    private void respondOnError(REP rep) throws IOException, ServerServiceException {
        ReqRsp rsp = ReqRsp.builder().rep(rep).build();
        outputStream.write(rsp.getBytes());
        outputStream.flush();
        outputStream.close();
        throw new ServerServiceException("Ruleset enforce error: " + rep.name());
    }

    public static RulesetEnforcerBuilder builder() {
        return new RulesetEnforcerBuilder();
    }

    public static final class RulesetEnforcerBuilder {
        private SOCKS5ServerRuleset ruleset;
        private OutputStream outputStream;
        private ReqRsp reqRsp;

        private RulesetEnforcerBuilder() {
        }

        public RulesetEnforcerBuilder ruleset(SOCKS5ServerRuleset ruleset) {
            this.ruleset = ruleset;
            return this;
        }

        public RulesetEnforcerBuilder outputStream(OutputStream outputStream) {
            this.outputStream = outputStream;
            return this;
        }

        public RulesetEnforcerBuilder clientRequest(ReqRsp reqRsp) {
            this.reqRsp = reqRsp;
            return this;
        }

        public RulesetEnforcer build() throws ServerServiceException {
            if (outputStream == null) throw new ServerServiceException("output stream not been set");
            if (ruleset == null) throw new ServerServiceException("ruleset has not been set");
            if (reqRsp == null) throw new ServerServiceException("clientRequest has not been set");

            return new RulesetEnforcer(ruleset, outputStream, reqRsp);
        }
    }
}
