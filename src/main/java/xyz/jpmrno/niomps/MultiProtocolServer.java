package xyz.jpmrno.niomps;

import xyz.jpmrno.niomps.dispatcher.Dispatcher;
import xyz.jpmrno.niomps.dispatcher.SubscriptionType;
import xyz.jpmrno.niomps.handlers.NCHandler;
import xyz.jpmrno.niomps.handlers.NCHandlerBuilder;
import xyz.jpmrno.niomps.io.Closeables;
import xyz.jpmrno.niomps.protocol.ProtocolHandlerBuilder;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class MultiProtocolServer implements Runnable {
    private final Map<Integer, ServerSocketChannel> channels;

    private final Dispatcher dispatcher;
    private final NCHandlerBuilder nchBuilder;

    private AtomicBoolean running = new AtomicBoolean(false);

    public MultiProtocolServer(final Dispatcher dispatcher, final NCHandlerBuilder nchBuilder) {
        this.channels = new HashMap<>();
        this.dispatcher = Objects.requireNonNull(dispatcher, "Dispatcher can't be null");
        this.nchBuilder = Objects.requireNonNull(nchBuilder, "Builder can't be null");
    }

    public void addProtocol(final int port, final ProtocolHandlerBuilder phBuilder) throws IOException {
        if (running.get()) {
            return;
        }

        if (!(port >= 1 && port <= 65535)) {
            throw new IllegalArgumentException("Port number should be between 1 and 65535 "
                    + "inclusive");
        }

        Objects.requireNonNull(phBuilder, "Protocol handler builder can't be null");

        ServerSocketChannel channel = ServerSocketChannel.open();

        NCHandler acceptHandler = nchBuilder.build(dispatcher, channel, phBuilder);
        acceptHandler.init();

        ServerSocketChannel prev = channels.put(port, channel);

        if (prev != null) {
            Closeables.closeSilently(prev);
        }
    }

    public void removeProtocolOnPort(final int port) {
        if (running.get()) {
            return;
        }

        ServerSocketChannel channel = channels.remove(port);
        Closeables.closeSilently(channel);
    }

    public void clearProtocols() {
        if (running.get()) {
            return;
        }

        for (Integer port : channels.keySet()) {
            ServerSocketChannel channel = channels.remove(port);
            Closeables.closeSilently(channel);
        }
    }

    @Override
    public void run() {
        if (running.get()) {
            return;
        }

        if (channels.isEmpty()) {
            throw new IllegalStateException("No protocols specified");
        }

        running.set(true);

        try {
            for (Integer port : channels.keySet()) {
                channels.get(port).bind(new InetSocketAddress(port));
            }
        } catch (Exception exception) {
            running.set(false);
            clearProtocols();
        }

        try {
            while (running.get()) {
                dispatcher.dispatch();
            }
        } catch (Exception exception) {
            // TODO: logger.error("Dispatcher force closed", exception);
        } finally {
            running.set(false);
            clearProtocols();
        }
    }

    public boolean isRunning() {
        return running.get();
    }
}
