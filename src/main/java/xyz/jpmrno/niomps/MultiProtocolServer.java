package xyz.jpmrno.niomps;

import xyz.jpmrno.niomps.dispatcher.Dispatcher;
import xyz.jpmrno.niomps.dispatcher.SubscriptionType;
import xyz.jpmrno.niomps.handlers.NewConnectionHandler;
import xyz.jpmrno.niomps.io.Closeables;

import java.io.IOException;
import java.nio.channels.ServerSocketChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class MultiProtocolServer implements Runnable {
    private final Map<Integer, ServerSocketChannel> sockets;

    private final Dispatcher dispatcher;

    private AtomicBoolean running = new AtomicBoolean(false);

    public MultiProtocolServer(Dispatcher dispatcher) {
        this.sockets = new HashMap<>();
        this.dispatcher = dispatcher;
    }

    public void addProtocol(int port, ProtocolHandlerFactory factory) throws IOException {
        if (running.get()) {
            return;
        }

        if (!(port >= 1 && port <= 65535)) {
            throw new IllegalArgumentException("Port number should be between 1 and 65535 "
                    + "inclusive");
        }

        Objects.requireNonNull(factory, "Protocol handler factory can't be null");

        ServerSocketChannel channel = ServerSocketChannel.open();
        channel.configureBlocking(false);

        ConnectionHandlerFactory connectionHandlers = new ProxyConnectionHandlerFactory(
                dispatcher, factory);
        NewConnectionHandler acceptHandler = new Acceptor(channel, dispatcher, connectionHandlers);

        dispatcher.subscribe(channel, acceptHandler).register(SubscriptionType.ACCEPT);

        ServerSocketChannel prev = sockets.put(port, channel);

        if (prev != null) {
            Closeables.closeSilently(prev);
        }
    }

    public void removeProtocolOnPort(int port) {
        if (running.get()) {
            return;
        }

        ServerSocketChannel channel = sockets.remove(port);
        Closeables.closeSilently(channel);
    }

    public void clearProtocols() {
        if (running.get()) {
            return;
        }

        for (Integer port : sockets.keySet()) {
            ServerSocketChannel channel = sockets.remove(port);
            Closeables.closeSilently(channel);
        }
    }

    @Override
    public void run() {
        if (sockets.isEmpty()) {
            throw new IllegalStateException("No protocols specified");
        }

        running.set(true);
    }

    public boolean isRunning() {
        return running.get();
    }
}
