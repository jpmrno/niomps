package xyz.jpmrno.niomps.handlers;

import xyz.jpmrno.niomps.dispatcher.SubscriptionManager;
import xyz.jpmrno.niomps.protocol.ProtocolBuilder;

import java.io.IOException;
import java.nio.channels.ServerSocketChannel;

public class AcceptorBuilder implements NCHandlerBuilder {
    @Override
    public NCHandler build(final SubscriptionManager manager, final ServerSocketChannel channel,
                           final ProtocolBuilder phBuilder) throws IOException {
        return new Acceptor(manager, channel, phBuilder);
    }
}
