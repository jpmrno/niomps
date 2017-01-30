package xyz.jpmrno.niomps.protocol;

import java.nio.ByteBuffer;

public interface ProtocolState {
    void afterAccept(final ProtocolContext context);

    default void afterConnect(final ProtocolContext context) {
        throw new UnsupportedOperationException("Handler has no connection support");
    }

    void afterRead(final ProtocolContext context, final ByteBuffer source);

    void afterWrite(final ProtocolContext context);

    void afterClose(final ProtocolContext context);
}
