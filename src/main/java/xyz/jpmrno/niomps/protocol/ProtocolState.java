package xyz.jpmrno.niomps.protocol;

import xyz.jpmrno.niomps.handlers.ActiveConnection;

import java.nio.ByteBuffer;

public interface ProtocolState {
    void afterConnect(ProtocolContext context);

    void afterRead(ProtocolContext context, ByteBuffer buffer);

    void afterWrite(ProtocolContext context);

    void afterClose(ProtocolContext context);

    void afterAccept(ProtocolContext context);
}
