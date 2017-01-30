package xyz.jpmrno.niomps.protocol.echo;

import xyz.jpmrno.niomps.protocol.Protocol;
import xyz.jpmrno.niomps.protocol.ProtocolHandler;
import xyz.jpmrno.niomps.protocol.ProtocolHandlerBuilder;

public class EchoProxyPB implements ProtocolHandlerBuilder {
    private static final EchoProxyClientPS state = new EchoProxyClientPS();

    @Override
    public ProtocolHandler build() {
        return new Protocol(state);
    }
}
