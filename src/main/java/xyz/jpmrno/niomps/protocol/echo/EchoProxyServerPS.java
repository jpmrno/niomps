package xyz.jpmrno.niomps.protocol.echo;

import xyz.jpmrno.niomps.protocol.ProtocolContext;

public class EchoProxyServerPS extends EchoProxyClientPS {
    @Override
    public void afterAccept(ProtocolContext context) {
        context.getConnection().requestRead();
    }
}
