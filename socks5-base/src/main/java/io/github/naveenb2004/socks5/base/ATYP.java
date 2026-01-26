package io.github.naveenb2004.socks5.base;

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
}
