package xyz.jpmrno.niomps.protocol;

import java.nio.ByteBuffer;

public interface ProtocolState {
    void afterAccept(final Protocol context);

    default void afterConnect(final Protocol context) {
        throw new UnsupportedOperationException("Handler has no connection support");
    }

    void afterRead(final Protocol context, final ByteBuffer source);

    void afterWrite(final Protocol context);

    void afterClose(final Protocol context);
}
