package xyz.jpmrno.niomps.protocol;

import xyz.jpmrno.niomps.handlers.ActiveConnection;

public interface ProtocolBuilder {
    Protocol build(ActiveConnection connection);
}
