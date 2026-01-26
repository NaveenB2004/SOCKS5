package io.github.naveenb2004.socks5.base.util;

import io.github.naveenb2004.socks5.base.CMD;
import io.github.naveenb2004.socks5.base.ATYP;
import io.github.naveenb2004.socks5.base.REP;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public final class ReqRsp {
    private final ReqRspField reqRspField;
    private final ATYP atyp;
    private final InetAddress addr;
    private final int port;

    private ReqRsp(ReqRspField reqRspField,
                   ATYP atyp,
                   InetAddress addr,
                   int port) {
        this.reqRspField = reqRspField;
        this.atyp = atyp;
        this.addr = addr;
        this.port = port;
    }

    public boolean isRequest() {
        return reqRspField instanceof CMD;
    }

    public boolean isResponse() {
        return reqRspField instanceof REP;
    }

    public ReqRspField getReqRspField() {
        return reqRspField;
    }

    public ATYP getAtyp() {
        return atyp;
    }

    public InetAddress getAddr() {
        return addr;
    }

    public int getPort() {
        return port;
    }

    public byte[] getBytes() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(0x05);
        baos.write(reqRspField.getValue());
        baos.write(0x00);
        baos.write(atyp == null ? 0x00 : atyp.getValue());
        baos.write(addr == null ? new byte[]{0x00} : addr.getAddress());
        baos.write(port);
        return baos.toByteArray();
    }

    public static ClientRequestBuilder builder() {
        return new ClientRequestBuilder();
    }

    public static final class ClientRequestBuilder {
        private ReqRspField reqRspField;
        private ATYP atyp;
        private InetAddress addr;
        private int port;

        private ClientRequestBuilder() {
        }

        public ClientRequestBuilder cmd(CMD cmd) {
            if (cmd == null) throw new IllegalArgumentException("cmd is null");
            this.reqRspField = cmd;
            return this;
        }

        public ClientRequestBuilder cmd(int cmd) {
            try {
                return cmd(CMD.valueOf(cmd));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public ClientRequestBuilder rep(REP rep) {
            if (rep == null) throw new IllegalArgumentException("rep is null");
            this.reqRspField = rep;
            return this;
        }

        public ClientRequestBuilder rep(int rep) {
            try {
                return rep(REP.valueOf(rep));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public ClientRequestBuilder atyp(ATYP atyp) {
            if (atyp == null) throw new IllegalArgumentException("atyp is null");
            this.atyp = atyp;
            return this;
        }

        public ClientRequestBuilder atyp(int atyp) {
            try {
                return atyp(ATYP.valueOf(atyp));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public ClientRequestBuilder addr(InetAddress addr) {
            if (addr == null) throw new IllegalArgumentException("addr is null");
            this.addr = addr;
            return this;
        }

        public ClientRequestBuilder addr(byte[] addr) {
            if (addr == null) throw new IllegalArgumentException("dstAddr must not be null");
            try {
                return addr(InetAddress.getByAddress(addr));
            } catch (UnknownHostException e) {
                throw new RuntimeException(e);
            }
        }

        public ClientRequestBuilder port(int port) {
            if (port < 0 || port > 65535) throw new IllegalArgumentException("port is out of range");
            this.port = port;
            return this;
        }

        public ClientRequestBuilder port(byte[] port) {
            if (port == null) throw new IllegalArgumentException("dstPort must not be null");
            if (port.length != 2) throw new IllegalArgumentException("port length must be 2");
            return port(((port[0] & 0xFF) << 8) | (port[1] & 0xFF));
        }

        public ReqRsp build() {
            return new ReqRsp(
                    reqRspField,
                    atyp,
                    addr,
                    port
            );
        }
    }
}
