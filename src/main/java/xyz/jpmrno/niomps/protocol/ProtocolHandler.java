package xyz.jpmrno.niomps.protocol;

import java.nio.ByteBuffer;

public interface ProtocolHandler {
    void afterConnect();

    void afterRead(final ByteBuffer buffer);

    void afterWrite();

    void afterClose();

    void afterAccept();
}
