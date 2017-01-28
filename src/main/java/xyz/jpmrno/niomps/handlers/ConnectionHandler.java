package xyz.jpmrno.niomps.handlers;

import xyz.jpmrno.niomps.dispatcher.Subscription;
import xyz.jpmrno.niomps.dispatcher.SubscriptionManager;
import xyz.jpmrno.niomps.dispatcher.SubscriptionType;
import xyz.jpmrno.niomps.protocol.ProtocolHandlerBuilder;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class ConnectionHandler implements ACHandler {
    private final SubscriptionManager manager;
    private final SocketChannel channel;
    private final ProtocolHandlerBuilder phBuilder;
    private final Subscription subscription;
    private boolean initialized = false;

    public ConnectionHandler(SubscriptionManager manager, final SocketChannel channel,
                             final ProtocolHandlerBuilder phBuilder) throws IOException {
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

        this.subscription.register(SubscriptionType.READ);
    }

    @Override
    public void connect() {

    }

    @Override
    public void read() {

    }

    @Override
    public void write() {

    }

    @Override
    public boolean requestWrite(ByteBuffer buffer) {
        return false;
    }

    @Override
    public boolean requestRead() {
        return false;
    }

    @Override
    public boolean requestClose() {
        return false;
    }

    @Override
    public boolean isAlive() {
        return false;
    }
}
