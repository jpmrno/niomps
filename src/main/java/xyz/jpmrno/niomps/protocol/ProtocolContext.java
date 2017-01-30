package xyz.jpmrno.niomps.protocol;

import xyz.jpmrno.niomps.handlers.ActiveConnection;

import java.nio.ByteBuffer;

public interface ProtocolContext {
    void setState(final ProtocolState state);

    ActiveConnection getConnection();

    ByteBuffer getBuffer();
}
