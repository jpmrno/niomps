package xyz.jpmrno.niomps.protocol;

import xyz.jpmrno.niomps.handlers.ActiveConnection;

public interface ProtocolContext {
    void setState(final ProtocolState state);

    ActiveConnection getConnection();
}
