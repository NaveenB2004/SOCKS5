package io.github.naveenb2004.socks5.server.config;

import io.github.naveenb2004.socks5.base.ATYP;
import io.github.naveenb2004.socks5.base.ImmutableObject;
import io.github.naveenb2004.socks5.base.CMD;
import io.github.naveenb2004.socks5.server.exception.SOCKS5ServerConfigException;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Set;

@ImmutableObject
public final class SOCKS5Ruleset {
    public enum RuleState {
        ALLOW,
        DENY
    }

    private final Set<CMD> commands;
    private final RuleState commandsState;
    private final Set<ATYP> addressTypes;
    private final RuleState addressTypesState;
    private final Set<byte[]> destAddresses;
    private final RuleState destAddressesState;
    private final Set<Integer> destPorts;
    private final RuleState destPortsState;

    private SOCKS5Ruleset(Set<CMD> commands,
                          RuleState commandsState,
                          Set<ATYP> addressTypes,
                          RuleState addressTypesState,
                          Set<byte[]> destAddresses,
                          RuleState destAddressesState,
                          Set<Integer> destPorts,
                          RuleState destPortsState) {
        this.commands = commands;
        this.commandsState = commandsState;
        this.addressTypes = addressTypes;
        this.addressTypesState = addressTypesState;
        this.destAddresses = destAddresses;
        this.destAddressesState = destAddressesState;
        this.destPorts = destPorts;
        this.destPortsState = destPortsState;
    }

    public Set<CMD> getCommands() {
        return commands;
    }

    public RuleState getCommandsState() {
        return commandsState;
    }

    public Set<ATYP> getAddressTypes() {
        return addressTypes;
    }

    public RuleState getAddressTypesState() {
        return addressTypesState;
    }

    public Set<byte[]> getDestAddresses() {
        return destAddresses;
    }

    public RuleState getDestAddressesState() {
        return destAddressesState;
    }

    public Set<Integer> getDestPorts() {
        return destPorts;
    }

    public RuleState getDestPortsState() {
        return destPortsState;
    }

    public static SOCKS5RulesetBuilder builder() {
        return new SOCKS5RulesetBuilder();
    }

    public static class SOCKS5RulesetBuilder {
        private final Set<CMD> commands = new HashSet<>();
        private RuleState commandsState;
        private final Set<ATYP> addressTypes = new HashSet<>();
        private RuleState addressTypesState;
        private final Set<byte[]> destAddresses = new HashSet<>();
        private RuleState destAddressesState;
        private final Set<Integer> destPorts = new HashSet<>();
        private RuleState destPortsState;

        private SOCKS5RulesetBuilder() {
        }

        public SOCKS5RulesetBuilder addCommand(CMD command) {
            if (command == null) throw new SOCKS5ServerConfigException("Command is null");
            commands.add(command);
            return this;
        }

        public SOCKS5RulesetBuilder commandsState(RuleState commandsState) {
            this.commandsState = commandsState;
            return this;
        }

        public SOCKS5RulesetBuilder addAddressType(ATYP addressType) {
            if (addressType == null) throw new SOCKS5ServerConfigException("AddressType is null");
            addressTypes.add(addressType);
            return this;
        }

        public SOCKS5RulesetBuilder addressTypesState(RuleState addressTypesState) {
            this.addressTypesState = addressTypesState;
            return this;
        }

        public SOCKS5RulesetBuilder addDestAddress(byte[] address) {
            if (address == null) throw new SOCKS5ServerConfigException("Address is null");
            destAddresses.add(address);
            return this;
        }

        public SOCKS5RulesetBuilder destAddressesState(RuleState destAddressesState) {
            this.destAddressesState = destAddressesState;
            return this;
        }

        public SOCKS5RulesetBuilder addDestPort(int port) {
            if (port < 1 || port > 65535) throw new SOCKS5ServerConfigException("Port is not in range");
            destPorts.add(port);
            return this;
        }

        public SOCKS5RulesetBuilder destPortsState(RuleState destPortsState) {
            this.destPortsState = destPortsState;
            return this;
        }

        public SOCKS5Ruleset build() {
            return new SOCKS5Ruleset(
                    commandsState == null ? null : Set.copyOf(commands), commandsState,
                    addressTypesState == null ? null : Set.copyOf(addressTypes), addressTypesState,
                    destAddressesState == null ? null : Set.copyOf(destAddresses), destAddressesState,
                    destPortsState == null ? null : Set.copyOf(destPorts), destPortsState
            );
        }
    }
}
