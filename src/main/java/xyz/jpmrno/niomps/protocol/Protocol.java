package xyz.jpmrno.niomps.protocol;

import xyz.jpmrno.niomps.handlers.ActiveConnection;

import java.nio.ByteBuffer;
import java.util.Objects;

public class Protocol implements ProtocolHandler, ProtocolContext {
    private static final int BUFFER_SIZE = 10000;

    private ProtocolState state;

    private ActiveConnection connection;
    private final ByteBuffer buffer;

    public Protocol(final ProtocolState state) {
        this(state, BUFFER_SIZE);
    }

    public Protocol(final ProtocolState state, int bufferSize) {
        this.state = Objects.requireNonNull(state);
        buffer = ByteBuffer.allocate(bufferSize);
    }

    @Override
    public void setState(final ProtocolState state) {
        this.state = Objects.requireNonNull(state);
    }

    @Override
    public ActiveConnection getConnection() {
        return connection;
    }

    @Override
    public ByteBuffer getBuffer() {
        return buffer;
    }

    @Override
    public void afterAccept(final ActiveConnection connection) {
        if (this.connection != null) {
            throw new IllegalStateException("Connection already accepted");
        }

        this.connection = Objects.requireNonNull(connection);
        this.state.afterAccept(this);
    }

    @Override
    public void afterConnect() {
        state.afterConnect(this);
    }

    @Override
    public void afterRead(final ByteBuffer buffer) {
        state.afterRead(this, buffer);
    }

    @Override
    public void afterWrite() {
        state.afterWrite(this);
    }

    @Override
    public void afterClose() {
        state.afterClose(this);
    }
}
