package xyz.jpmrno.niomps.protocol;

import xyz.jpmrno.niomps.handlers.ActiveConnection;

import java.nio.ByteBuffer;

public interface ProtocolHandler {
    void afterAccept(ActiveConnection connection);

    void afterConnect();

    void afterRead(final ByteBuffer source);

    void afterWrite();

    void afterClose();
}
