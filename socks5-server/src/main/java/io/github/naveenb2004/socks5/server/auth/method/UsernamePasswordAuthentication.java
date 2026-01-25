package io.github.naveenb2004.socks5.server.auth.method;

import io.github.naveenb2004.socks5.server.auth.SOCKS5ServerAuth;
import io.github.naveenb2004.socks5.server.exception.SOCKS5ServerException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

public record UsernamePasswordAuthentication(Map<String, String> usernamePassword) implements SOCKS5ServerAuth {
    @Override
    public byte getMethodId() {
        return 0x02;
    }

    @Override
    public void authenticateAsServer(InputStream inputStream,
                                     OutputStream outputStream) {
        try {
            int ver = inputStream.read();
            if (ver != 0x01) {
                sendAuthResponse(outputStream, 0x01);
                return;
            }

            int ulen = inputStream.read();
            byte[] uname = new byte[ulen];
            int readUlen = inputStream.read(uname);
            if (readUlen != ulen) {
                sendAuthResponse(outputStream, 0x02);
                return;
            }

            int plen = inputStream.read();
            byte[] passwd = new byte[plen];
            int readPlen = inputStream.read(passwd);
            if (readPlen != plen) {
                sendAuthResponse(outputStream, 0x03);
                return;
            }

            String username = new String(uname);
            String password = usernamePassword.get(username);
            if (password == null || !password.equals(new String(passwd))) {
                sendAuthResponse(outputStream, 0x04);
                return;
            }

            sendAuthResponse(outputStream, 0x00);
        } catch (IOException e) {
            throw new SOCKS5ServerException(e);
        }
    }

    private void sendAuthResponse(OutputStream outputStream,
                                  int response) throws IOException {
        outputStream.write(0x01);
        outputStream.write(response);
        outputStream.flush();
        if (response != 0x00) {
            outputStream.close();
        }
    }

    @Override
    public InputStream getUnwrapperServerInputStream(InputStream inputStream) {
        return inputStream;
    }

    @Override
    public OutputStream getWrapperServerOutputStream(OutputStream outputStream) {
        return outputStream;
    }
}
