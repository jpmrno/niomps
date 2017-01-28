package xyz.jpmrno.niomps.handlers;

import xyz.jpmrno.niomps.protocol.ProtocolHandler;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

public interface SocketConnection {
    default boolean requestConnect(final InetSocketAddress address,
                                   final ProtocolHandler serverProtocol) {
        throw new UnsupportedOperationException();
    }

    boolean requestWrite(ByteBuffer buffer);

    boolean requestRead();

    boolean requestClose();

    boolean isAlive();
}
