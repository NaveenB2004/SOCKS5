package io.github.naveenb2004.socks5.server.config;

import io.github.naveenb2004.socks5.base.CMD;
import io.github.naveenb2004.socks5.base.ATYP;
import io.github.naveenb2004.socks5.server.SOCKS5ServerException;

import java.net.InetAddress;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class SOCKS5ServerRuleset {
    private final boolean filterCommands;
    private final Set<CMD> filterCommandTargets;
    private final boolean filterAddressTypes;
    private final Set<ATYP> filterAddressTypeTargets;
    private final Boolean filterHosts;
    private final Set<InetAddress> filterHostTargets;
    private final Boolean filterPorts;
    private final Set<Integer> filterPortTargets;

    private SOCKS5ServerRuleset(boolean filterCommands,
                                Set<CMD> filterCommandTargets,
                                boolean filterAddressTypes,
                                Set<ATYP> filterAddressTypeTargets,
                                Boolean filterHosts,
                                Set<InetAddress> filterHostTargets,
                                Boolean filterPorts,
                                Set<Integer> filterPortTargets) {
        this.filterCommands = filterCommands;
        this.filterCommandTargets = filterCommandTargets;
        this.filterAddressTypes = filterAddressTypes;
        this.filterAddressTypeTargets = filterAddressTypeTargets;
        this.filterHosts = filterHosts;
        this.filterHostTargets = filterHostTargets;
        this.filterPorts = filterPorts;
        this.filterPortTargets = filterPortTargets;
    }

    public boolean isFilterCommands() {
        return filterCommands;
    }

    public Set<CMD> getFilterCommandTargets() {
        return filterCommandTargets;
    }

    public boolean isFilterAddressTypes() {
        return filterAddressTypes;
    }

    public Set<ATYP> getFilterAddressTypeTargets() {
        return filterAddressTypeTargets;
    }

    public Boolean getFilterHosts() {
        return filterHosts;
    }

    public boolean isFilterHostsAllowed() {
        return filterHosts;
    }

    public boolean isFilterHostsDenied() {
        return !filterHosts;
    }

    public Set<InetAddress> getFilterHostTargets() {
        return filterHostTargets;
    }

    public Boolean getFilterPorts() {
        return filterPorts;
    }

    public boolean isFilterPortsAllowed() {
        return filterPorts;
    }

    public boolean isFilterPortsDenied() {
        return !filterPorts;
    }

    public Set<Integer> getFilterPortTargets() {
        return filterPortTargets;
    }

    public static SOCKS5ServerRulesetBuilder builder() {
        return new SOCKS5ServerRulesetBuilder();
    }

    public static final class SOCKS5ServerRulesetBuilder {
        private boolean filterCommands;
        private final Set<CMD> filterCommandTargets;
        private boolean filterAddressTypes;
        private final Set<ATYP> filterAddressTypeTargets;
        private Boolean filterHosts;
        private final Set<InetAddress> filterHostTargets;
        private Boolean filterPorts;
        private final Set<Integer> filterPortTargets;

        private SOCKS5ServerRulesetBuilder() {
            this.filterCommandTargets = new HashSet<>(1, 1);
            this.filterAddressTypeTargets = new HashSet<>(1, 1);
            this.filterHostTargets = new HashSet<>(1, 1);
            this.filterPortTargets = new HashSet<>(1, 1);
        }

        public SOCKS5ServerRulesetBuilder filterCommands(boolean filterCommands) {
            this.filterCommands = filterCommands;
            return this;
        }

        public SOCKS5ServerRulesetBuilder filterCommand(CMD filterCommand) {
            if (filterCommand == null) throw new SOCKS5ServerException("filterCommand is null");
            filterCommandTargets.add(filterCommand);
            return this;
        }

        public SOCKS5ServerRulesetBuilder filterAddressTypes(boolean filterAddressTypes) {
            this.filterAddressTypes = filterAddressTypes;
            return this;
        }

        public SOCKS5ServerRulesetBuilder filterAddressType(ATYP filterAddressType) {
            if (filterAddressType == null) throw new SOCKS5ServerException("filterAddressType is null");
            filterAddressTypeTargets.add(filterAddressType);
            return this;
        }

        public SOCKS5ServerRulesetBuilder filterHosts(Boolean filterHosts) {
            this.filterHosts = filterHosts;
            return this;
        }

        public SOCKS5ServerRulesetBuilder filterHost(InetAddress filterHost) {
            if (filterHosts == null) throw new SOCKS5ServerException("filterHosts is null");
            filterHostTargets.add(filterHost);
            return this;
        }

        public SOCKS5ServerRulesetBuilder filterPorts(Boolean filterPorts) {
            this.filterPorts = filterPorts;
            return this;
        }

        public SOCKS5ServerRulesetBuilder filterPort(int filterPort) {
            if (filterPort < 1 || filterPort > 65535) throw new SOCKS5ServerException("filterPort is out of range");
            filterPortTargets.add(filterPort);
            return this;
        }

        public SOCKS5ServerRuleset build() {
            return new SOCKS5ServerRuleset(
                    filterCommands,
                    filterCommandTargets,
                    filterAddressTypes,
                    filterAddressTypeTargets,
                    filterHosts,
                    filterHostTargets,
                    filterPorts,
                    filterPortTargets
            );
        }
    }
}
