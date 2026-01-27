package io.github.naveenb2004.socks5.base.command;

public enum CMD {
    CONNECT(0x01),
    BIND(0x02),
    UDP_ASSOCIATE(0x03);

    private final int value;

    CMD(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static CMD valueOf(int value) {
        for (CMD cmd : CMD.values()) {
            if (cmd.getValue() == value) {
                return cmd;
            }
        }
        return null;
    }
}
