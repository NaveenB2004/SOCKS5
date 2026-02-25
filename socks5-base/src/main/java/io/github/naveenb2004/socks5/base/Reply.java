/*
 * Copyright (c) 2026 Naveen N. Balasooriya
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
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
