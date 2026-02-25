/*
 * Copyright (c) 2026 Naveen N. Balasooriya
 *
 * This source code is licensed under the MIT License.
 * See the LICENSE file in the project root for full license information.
 */

package io.github.naveenb2004.socks5.base;

public enum Command {
    CONNECT(0x01),
    BIND(0x02),
    UDP_ASSOCIATE(0x03);

    private final int value;

    Command(final int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static Command fromValue(final int value) {
        return switch (value) {
            case 0x01 -> CONNECT;
            case 0x02 -> BIND;
            case 0x03 -> UDP_ASSOCIATE;
            default -> null;
        };
    }
}
