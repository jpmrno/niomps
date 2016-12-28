package xyz.jpmrno.niomps.dispatcher;

import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;

public class Subscription {
    private final Selector selector;
    private final SelectableChannel channel;
    private final Subscriber handler;

    private boolean cancelled = false;

    public Subscription(Selector selector, SelectableChannel channel, Subscriber handler)
            throws ClosedChannelException {
        this.selector = checkNotNull(selector, "Selector can't be null");
        this.channel = checkNotNull(channel, "Channel can't be null");
        this.handler = checkNotNull(handler, "Subscriber can't be null");

        channel.register(selector, DispatcherOperation.NONE.getValue(), handler);
    }

    void add(final DispatcherOperation op) throws SubscriptionException {
        if (cancelled) {
            return;
        }

        checkNotNull(op, "Operation can't be null");

        SelectionKey key = channel.keyFor(selector);

        if (key != null) {
            key.interestOps(key.interestOps() | op.getValue());
        } else {
            cancelled = true;
        }
    }

    void remove(final DispatcherOperation op) {
        if (cancelled) {
            return;
        }

        checkNotNull(op, "Operation can't be null");

        SelectionKey key = channel.keyFor(selector);

        if (key != null) {
            key.interestOps(key.interestOps() & ~op.getValue());
        } else {
            cancelled = true;
        }
    }

    void cancel() {
        if (cancelled) {
            return;
        }

        cancelled = true;

        SelectionKey key = channel.keyFor(selector);

        if (key != null) {
            key.cancel();
        }
    }

    boolean isCancelled() {
        return cancelled;
    }
}
