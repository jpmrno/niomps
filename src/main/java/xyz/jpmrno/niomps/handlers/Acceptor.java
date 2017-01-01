package xyz.jpmrno.niomps.handlers;

import xyz.jpmrno.niomps.dispatcher.SubscriptionManager;
import xyz.jpmrno.niomps.dispatcher.SubscriptionType;
import xyz.jpmrno.niomps.io.Closeables;
import xyz.jpmrno.niomps.protocol.ProtocolHandlerBuilder;

import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class Acceptor implements NCHandler {
    private final SubscriptionManager manager;
    private final ServerSocketChannel channel;
    private final ProtocolHandlerBuilder phBuilder;

    public Acceptor(final SubscriptionManager manager, final ServerSocketChannel channel,
                    final ProtocolHandlerBuilder phBuilder) {
        this.manager = manager;
        this.channel = channel;
        this.phBuilder = phBuilder;
    }

    @Override
    public void accept() {
        SocketChannel client;

        try {
            client = channel.accept();
        } catch (Exception exception) {
            // TODO
            return;
        }

        try {
            if (client != null) {
                client.configureBlocking(false);

                ACHandler acHandler = new ConnectionHandler(client, phBuilder);
                manager.subscribe(client, acHandler).register(SubscriptionType.READ);
            }
        } catch (Exception exception) {
            Closeables.closeSilently(client);
        }
    }
}
