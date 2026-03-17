/*
 * Copyright (c) 2026 Naveen N. Balasooriya
 *
 * This source code is licensed under the MIT License.
 * See the LICENSE file in the project root for full license information.
 */

package net.naveenb2004.socks5.base;

public enum AddressType {
    IPv4(0x01),
    DOMAIN_NAME(0x03),
    IPv6(0x04);

    private final int value;

    AddressType(final int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static AddressType fromValue(final int value) {
        return switch (value) {
            case 0x01 -> IPv4;
            case 0x03 -> DOMAIN_NAME;
            case 0x04 -> IPv6;
            default -> null;
        };
    }
}
