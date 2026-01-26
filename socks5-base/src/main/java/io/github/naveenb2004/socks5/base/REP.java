package io.github.naveenb2004.socks5.base;

import io.github.naveenb2004.socks5.base.util.ReqRspField;

public enum REP implements ReqRspField {
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

    REP(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static REP valueOf(int value) {
        for (REP rep : REP.values()) {
            if (rep.value == value) {
                return rep;
            }
        }
        throw new IllegalArgumentException("Unknown REP value: " + value);
    }
}
