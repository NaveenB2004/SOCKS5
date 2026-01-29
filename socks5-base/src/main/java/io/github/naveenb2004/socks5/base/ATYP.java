package io.github.naveenb2004.socks5.base;

import io.github.naveenb2004.socks5.base.exception.SOCKS5Exception;

public enum ATYP {
    IP_V4_ADDRESS(0x01),
    DOMAINNAME(0x03),
    IP_V6_ADDRESS(0x04);

    private final int value;

    ATYP(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static ATYP valueOf(int value) {
        for (ATYP atyp : ATYP.values()) {
            if (atyp.getValue() == value) {
                return atyp;
            }
        }
        throw new SOCKS5Exception("Invalid ATYP value");
    }
}
