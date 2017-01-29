package xyz.jpmrno.niomps.protocol;

import xyz.jpmrno.niomps.handlers.ActiveConnection;

public interface ProtocolContext {
    ActiveConnection getConnection();

    void setState(ProtocolState state);
}
