package xyz.jpmrno.niomps.handlers;

import xyz.jpmrno.niomps.protocol.Protocol;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

public interface ActiveConnection {
    default boolean requestConnect(final InetSocketAddress address,
                                   final Protocol otherProtocol) throws IOException {
        throw new UnsupportedOperationException();
    }

    boolean requestWrite(ByteBuffer buffer);

    boolean requestRead();

    boolean requestClose();

    boolean isAlive();
}
