package xyz.jpmrno.niomps.protocol.echo;

import xyz.jpmrno.niomps.handlers.ActiveConnection;
import xyz.jpmrno.niomps.protocol.Protocol;
import xyz.jpmrno.niomps.protocol.ProtocolContext;
import xyz.jpmrno.niomps.protocol.ProtocolState;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

public class EchoProxyClientPS implements ProtocolState {
    private static final String HOSTNAME = "localhost";
    private static final int PORT = 5000;

    @Override
    public void afterAccept(final ProtocolContext context) {
        ActiveConnection connection = context.getConnection();
        InetSocketAddress address = new InetSocketAddress(HOSTNAME, PORT);
        Protocol protocol = new Protocol(this);
        if (!connection.requestConnect(address, protocol)) {
            connection.requestClose();
        }
    }

    @Override
    public void afterConnect(final ProtocolContext context) {
        context.getConnection().requestRead();
    }

    @Override
    public void afterRead(final ProtocolContext context, final ByteBuffer source) {
        // TODO
    }

    @Override
    public void afterWrite(final ProtocolContext context) {
        // TODO
    }

    @Override
    public void afterClose(final ProtocolContext context) {
        // TODO
    }
}
