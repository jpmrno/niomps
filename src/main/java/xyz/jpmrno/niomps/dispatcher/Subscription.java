package xyz.jpmrno.niomps.dispatcher;

import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Objects;

public class Subscription {
    private final Selector selector;
    private final SelectableChannel channel;

    private boolean cancelled = false;

    public Subscription(Selector selector, SelectableChannel channel, Subscriber handler)
            throws ClosedChannelException {

        this.selector = Objects.requireNonNull(selector, "Selector can't be null");
        this.channel = Objects.requireNonNull(channel, "Channel can't be null");
        Objects.requireNonNull(handler, "Subscriber can't be null");

        channel.register(selector, SubscriptionType.NONE.getValue(), handler);
    }

    public void register(final SubscriptionType op) {
        if (cancelled) {
            return;
        }

        Objects.requireNonNull(op, "Operation can't be null");

        SelectionKey key = channel.keyFor(selector);

        if (key != null) {
            key.interestOps(key.interestOps() | op.getValue());
        } else {
            cancelled = true;
        }
    }

    public void unregister(final SubscriptionType op) {
        if (cancelled) {
            return;
        }

        Objects.requireNonNull(op, "Operation can't be null");

        SelectionKey key = channel.keyFor(selector);

        if (key != null) {
            key.interestOps(key.interestOps() & ~op.getValue());
        } else {
            cancelled = true;
        }
    }

    public void cancel() {
        if (cancelled) {
            return;
        }

        cancelled = true;

        SelectionKey key = channel.keyFor(selector);

        if (key != null) {
            key.cancel();
        }
    }

    public boolean isCancelled() {
        return cancelled;
    }
}
