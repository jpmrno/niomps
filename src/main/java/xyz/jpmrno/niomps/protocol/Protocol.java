package xyz.jpmrno.niomps.protocol;

import xyz.jpmrno.niomps.handlers.ActiveConnection;

import java.nio.ByteBuffer;
import java.util.Objects;

public class Protocol implements ProtocolHandler, ProtocolContext {
    private ActiveConnection connection;
    private ProtocolState state;

    public Protocol(final ProtocolState state) {
        this.state = Objects.requireNonNull(state);
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
