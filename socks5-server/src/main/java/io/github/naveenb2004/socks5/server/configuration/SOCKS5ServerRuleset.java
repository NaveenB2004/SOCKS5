/*
 * Copyright (c) 2026 Naveen N. Balasooriya
 *
 * This source code is licensed under the MIT License.
 * See the LICENSE file in the project root for full license information.
 */

package io.github.naveenb2004.socks5.server.configuration;

import io.github.naveenb2004.socks5.base.AddressType;
import io.github.naveenb2004.socks5.base.Command;
import io.github.naveenb2004.socks5.server.exception.SOCKS5ServerException;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Set;

public final class SOCKS5ServerRuleset {
    private final Boolean enforceCommands;
    private final Set<Command> commands;
    private final Boolean enforceAddressTypes;
    private final Set<AddressType> addressTypes;
    private final Boolean enforceAddresses;
    private final Set<InetAddress> addresses;
    private final Boolean enforcePorts;
    private final Set<Integer> ports;
    private final Boolean enforceDestinations;
    private final Set<InetSocketAddress> destinations;

    private SOCKS5ServerRuleset(final Boolean enforceCommands,
                                final Set<Command> commands,
                                final Boolean enforceAddressTypes,
                                final Set<AddressType> addressTypes,
                                final Boolean enforceAddresses,
                                final Set<InetAddress> addresses,
                                final Boolean enforcePorts,
                                final Set<Integer> ports,
                                final Boolean enforceDestinations,
                                final Set<InetSocketAddress> destinations) {
        this.enforceCommands = enforceCommands;
        this.commands = commands;
        this.enforceAddressTypes = enforceAddressTypes;
        this.addressTypes = addressTypes;
        this.enforceAddresses = enforceAddresses;
        this.addresses = addresses;
        this.enforcePorts = enforcePorts;
        this.ports = ports;
        this.enforceDestinations = enforceDestinations;
        this.destinations = destinations;
    }

    public Boolean getEnforceCommands() {
        return enforceCommands;
    }

    public Set<Command> getCommands() {
        return commands;
    }

    public Boolean getEnforceAddressTypes() {
        return enforceAddressTypes;
    }

    public Set<AddressType> getAddressTypes() {
        return addressTypes;
    }

    public Boolean getEnforceAddresses() {
        return enforceAddresses;
    }

    public Set<InetAddress> getAddresses() {
        return addresses;
    }

    public Boolean getEnforcePorts() {
        return enforcePorts;
    }

    public Set<Integer> getPorts() {
        return ports;
    }

    public Boolean getEnforceDestinations() {
        return enforceDestinations;
    }

    public Set<InetSocketAddress> getDestinations() {
        return destinations;
    }

    public static SOCKS5ServerRulesetBuilder builder() {
        return new SOCKS5ServerRulesetBuilder();
    }

    public static final class SOCKS5ServerRulesetBuilder {
        private Boolean enforceCommands;
        private final Set<Command> commands = new HashSet<>(1, 1);
        private Boolean enforceAddressTypes;
        private final Set<AddressType> addressTypes = new HashSet<>(1, 1);
        private Boolean enforceAddresses;
        private final Set<InetAddress> addresses = new HashSet<>(1, 1);
        private Boolean enforcePorts;
        private final Set<Integer> ports = new HashSet<>(1, 1);
        private Boolean enforceDestinations;
        private final Set<InetSocketAddress> destinations = new HashSet<>(1, 1);

        private SOCKS5ServerRulesetBuilder() {
        }

        public SOCKS5ServerRulesetBuilder enforceCommands(final Boolean enforceCommands) {
            this.enforceCommands = enforceCommands;
            return this;
        }

        public SOCKS5ServerRulesetBuilder command(final Command command) {
            if (command == null) throw new SOCKS5ServerException("Command cannot be null");
            commands.add(command);
            return this;
        }

        public SOCKS5ServerRulesetBuilder enforceAddressTypes(final Boolean enforceAddressTypes) {
            this.enforceAddressTypes = enforceAddressTypes;
            return this;
        }

        public SOCKS5ServerRulesetBuilder addressType(final AddressType addressType) {
            if (addressType == null) throw new SOCKS5ServerException("Address type cannot be null");
            addressTypes.add(addressType);
            return this;
        }

        public SOCKS5ServerRulesetBuilder enforceAddresses(final Boolean enforceAddresses) {
            this.enforceAddresses = enforceAddresses;
            return this;
        }

        public SOCKS5ServerRulesetBuilder address(final InetAddress address) {
            if (address == null) throw new SOCKS5ServerException("Address cannot be null");
            addresses.add(address);
            return this;
        }

        public SOCKS5ServerRulesetBuilder enforcePorts(final Boolean enforcePorts) {
            this.enforcePorts = enforcePorts;
            return this;
        }

        public SOCKS5ServerRulesetBuilder port(final int port) {
            if (port < 0 || port > 65535) throw new SOCKS5ServerException("Invalid port: " + port);
            ports.add(port);
            return this;
        }

        public SOCKS5ServerRulesetBuilder enforceDestinations(final Boolean enforceDestinations) {
            this.enforceDestinations = enforceDestinations;
            return this;
        }

        public SOCKS5ServerRulesetBuilder destination(final InetSocketAddress destination) {
            if (destination == null) throw new SOCKS5ServerException("Destination cannot be null");
            destinations.add(destination);
            return this;
        }

        public SOCKS5ServerRuleset build() {
            if (enforceCommands != null && commands.isEmpty()) {
                throw new SOCKS5ServerException("Commands are enforced but not available");
            }
            if (enforceAddressTypes != null && addressTypes.isEmpty()) {
                throw new SOCKS5ServerException("AddressTypes are enforced but not available");
            }
            if (enforceAddresses != null && addresses.isEmpty()) {
                throw new SOCKS5ServerException("Addresses are enforced but not available");
            }
            if (enforcePorts != null && ports.isEmpty()) {
                throw new SOCKS5ServerException("Ports are enforced but not available");
            }
            if (enforceDestinations != null && destinations.isEmpty()) {
                throw new SOCKS5ServerException("Destinations are enforced but not available");
            }

            return new SOCKS5ServerRuleset(
                    enforceCommands,
                    enforceCommands != null ? Set.copyOf(commands) : null,
                    enforceAddressTypes,
                    enforceAddressTypes != null ? Set.copyOf(addressTypes) : null,
                    enforceAddresses,
                    enforceAddresses != null ? Set.copyOf(addresses) : null,
                    enforcePorts,
                    enforcePorts != null ? Set.copyOf(ports) : null,
                    enforceDestinations,
                    enforceDestinations != null ? Set.copyOf(destinations) : null
            );
        }
    }
}
