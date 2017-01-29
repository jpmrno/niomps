package xyz.jpmrno.niomps.handlers;

import xyz.jpmrno.niomps.dispatcher.Subscription;
import xyz.jpmrno.niomps.dispatcher.SubscriptionManager;
import xyz.jpmrno.niomps.dispatcher.SubscriptionType;
import xyz.jpmrno.niomps.io.Closeables;
import xyz.jpmrno.niomps.protocol.Protocol;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class ConnectionHandler implements ACHandler, ActiveConnection {
    private static final int READ_BUFFER_SIZE = 1024;
    private static final int WRITE_BUFFER_SIZE = 1024;

    private final SubscriptionManager manager;
    private final SocketChannel channel;
    private final Protocol protocol;
    private final Subscription subscription;
    private final ByteBuffer readBuffer;
    private final ByteBuffer writeBuffer;

    private boolean initialized = false;
    private boolean closeRequested = false;

    public ConnectionHandler(final SubscriptionManager manager, final SocketChannel channel,
                             final Protocol protocol) throws IOException {
        channel.configureBlocking(false);

        this.manager = manager;
        this.channel = channel;
        this.protocol = protocol;
        this.subscription = manager.subscribe(channel, this);
        readBuffer = ByteBuffer.allocate(READ_BUFFER_SIZE);
        writeBuffer = ByteBuffer.allocate(WRITE_BUFFER_SIZE);
    }

    @Override
    public void init() {
        if (initialized) {
            throw new IllegalStateException("Already initialized");
        }

        protocol.afterAccept();
    }

    @Override
    public boolean requestConnect(final InetSocketAddress address, final Protocol otherProtocol) {
        SocketChannel otherChannel = null;
        try {
            otherChannel = SocketChannel.open();
            otherChannel.configureBlocking(false);
            otherChannel.connect(address);
        } catch (Exception exception) {
            if (otherChannel != null) {
                Closeables.closeSilently(otherChannel);
            }

            return false;
        }

        ConnectionHandler otherHandler = new ConnectionHandler(manager, otherChannel, otherProtocol);
        otherHandler.subscription.register(SubscriptionType.CONNECT);

        return true;
    }

    @Override
    public void connect() {
        try {
            if (channel.finishConnect()) {
                subscription.unregister(SubscriptionType.CONNECT);
                protocol.afterConnect();
            }
        } catch (Exception exception) {
            forceClose();
        }
    }

    @Override
    public boolean requestRead() {
        if (closeRequested || channel.isConnectionPending() || !channel.isOpen()) {
            return false;
        }

        subscription.register(SubscriptionType.READ);

        return true;
    }

    @Override
    public void read() {
        int bytesRead;

        try {
            bytesRead = channel.read(readBuffer);
        } catch (IOException exception) {
            forceClose();
            return;
        }

        if (bytesRead == -1) {
            forceClose();
            return;
        }

        if (bytesRead > 0) {
            subscription.unregister(SubscriptionType.READ);

            readBuffer.flip();
            protocol.afterRead(readBuffer);
            readBuffer.clear();
        }
    }

    @Override
    public boolean requestWrite(ByteBuffer source) {
        if (closeRequested || channel.isConnectionPending() || !channel.isOpen()) {
            return false;
        }

        while (writeBuffer.position() < writeBuffer.limit() && source.hasRemaining()) {
            writeBuffer.put(source.get());
        }

        subscription.register(SubscriptionType.WRITE);

        return true;
    }

    @Override
    public void write() {
        writeBuffer.flip();
        try {
            channel.write(writeBuffer);
        } catch (IOException exception) {
            forceClose();
            return;
        }
        writeBuffer.compact();

        if (!writeBuffer.hasRemaining()) {
            subscription.unregister(SubscriptionType.WRITE);

            if (closeRequested) {
                forceClose();
                return;
            }
        }

        // TODO: Only if !writeBuffer.hasRemaining()??
        protocol.afterWrite();
    }

    @Override
    public boolean requestClose() {
        return false;
    }

    private void forceClose() {
        Closeables.closeSilently(channel);
        protocol.afterClose();
    }

    @Override
    public boolean isAlive() {
        return channel.isOpen();
    }
}
