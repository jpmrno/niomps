package xyz.jpmrno.niomps.handlers;

import xyz.jpmrno.niomps.dispatcher.Subscription;
import xyz.jpmrno.niomps.dispatcher.SubscriptionManager;
import xyz.jpmrno.niomps.dispatcher.SubscriptionType;
import xyz.jpmrno.niomps.io.Closeables;
import xyz.jpmrno.niomps.protocol.Protocol;
import xyz.jpmrno.niomps.protocol.ProtocolBuilder;

import java.io.IOException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class Acceptor implements NCHandler {
    private final SubscriptionManager manager;
    private final ServerSocketChannel channel;
    private final ProtocolBuilder phBuilder;
    private final Subscription subscription;
    private boolean initialized = false;

    public Acceptor(final SubscriptionManager manager, final ServerSocketChannel channel,
                    final ProtocolBuilder phBuilder) throws IOException {
        channel.configureBlocking(false);

        this.manager = manager;
        this.channel = channel;
        this.phBuilder = phBuilder;
        this.subscription = manager.subscribe(channel, this);
    }

    @Override
    public void init() {
        if (initialized) {
            throw new IllegalStateException("Already initialized");
        }

        subscription.register(SubscriptionType.ACCEPT);
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
                Protocol protocol = phBuilder.build();
                ACHandler acHandler = new ConnectionHandler(manager, client, protocol);
                acHandler.init();
            }
        } catch (Exception exception) {
            Closeables.closeSilently(client);
        }
    }
}
