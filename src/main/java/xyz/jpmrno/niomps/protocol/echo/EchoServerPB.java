package xyz.jpmrno.niomps.protocol.echo;

import xyz.jpmrno.niomps.protocol.Protocol;
import xyz.jpmrno.niomps.protocol.ProtocolHandler;
import xyz.jpmrno.niomps.protocol.ProtocolHandlerBuilder;
import xyz.jpmrno.niomps.protocol.ProtocolState;

public class EchoServerPB implements ProtocolHandlerBuilder {
    private static final ProtocolState state = new EchoServerPS();

    @Override
    public ProtocolHandler build() {
        return new Protocol(state);
    }
}
