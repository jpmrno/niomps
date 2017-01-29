package xyz.jpmrno.niomps.protocol;

import xyz.jpmrno.niomps.handlers.ActiveConnection;

import java.nio.ByteBuffer;
import java.util.Objects;

public class Protocol implements ProtocolHandler, ProtocolContext {
    private final ActiveConnection connection;
    private ProtocolState state;

    public Protocol(ActiveConnection connection, ProtocolState state) {
        this.connection = Objects.requireNonNull(connection);
        this.state = Objects.requireNonNull(state);
    }

    @Override
    public ActiveConnection getConnection() {
        return connection;
    }

    @Override
    public void setState(ProtocolState state) {
        this.state = Objects.requireNonNull(state);
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

    @Override
    public void afterAccept() {
        state.afterAccept(this);
    }
}
