package xyz.jpmrno.niomps.protocol;

import xyz.jpmrno.niomps.protocol.echo.ProxyProtocolState;

public class ProxyProtocol extends Protocol {
    private ProxyProtocol otherProtocol;

    public ProxyProtocol(ProxyProtocolState state) {
        super(state);
    }

    ProxyProtocol getOtherProtocol() {
        return otherProtocol;
    }
}
