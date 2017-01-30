package xyz.jpmrno.niomps.handlers;

import xyz.jpmrno.niomps.dispatcher.SubscriptionManager;
import xyz.jpmrno.niomps.protocol.ProtocolHandlerBuilder;

import java.io.IOException;
import java.nio.channels.ServerSocketChannel;

public interface NCHandlerBuilder {
    NCHandler build(final SubscriptionManager manager, final ServerSocketChannel channel,
                    final ProtocolHandlerBuilder phBuilder) throws IOException;
}
