/*
 * Copyright (c) 2026 Naveen N. Balasooriya
 *
 * This source code is licensed under the MIT License.
 * See the LICENSE file in the project root for full license information.
 */

package io.github.naveenb2004.socks5.base;

public enum Reply {
    SUCCEEDED(0x00),
    GENERAL_SOCKS_SERVER_FAILURE(0x01),
    CONNECTION_NOT_ALLOWED_BY_RULESET(0x02),
    NETWORK_UNREACHABLE(0x03),
    HOST_UNREACHABLE(0x04),
    CONNECTION_REFUSED(0x05),
    TTL_EXPIRED(0x06),
    COMMAND_NOT_SUPPORTED(0x07),
    ADDRESS_TYPE_NOT_SUPPORTED(0x08);

    private final int value;

    Reply(final int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static Reply valueOf(final int value) {
        return switch (value) {
            case 0x00 -> SUCCEEDED;
            case 0x01 -> GENERAL_SOCKS_SERVER_FAILURE;
            case 0x02 -> CONNECTION_NOT_ALLOWED_BY_RULESET;
            case 0x03 -> NETWORK_UNREACHABLE;
            case 0x04 -> HOST_UNREACHABLE;
            case 0x05 -> CONNECTION_REFUSED;
            case 0x06 -> TTL_EXPIRED;
            case 0x07 -> COMMAND_NOT_SUPPORTED;
            case 0x08 -> ADDRESS_TYPE_NOT_SUPPORTED;
            default -> null;
        };
    }
}
