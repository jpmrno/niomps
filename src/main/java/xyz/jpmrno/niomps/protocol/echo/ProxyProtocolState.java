package xyz.jpmrno.niomps.protocol.echo;

import xyz.jpmrno.niomps.protocol.ProtocolState;

import java.nio.ByteBuffer;

public interface ProxyProtocolState extends ProtocolState {
    void requestRead();

    void requestWrite(final ByteBuffer buffer, final Runnable action);

    void requestClose();
}
