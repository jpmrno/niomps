package xyz.jpmrno.niomps.dispatcher;

import java.nio.channels.SelectableChannel;

public interface SubscriptionManager {
    Subscription subscribe(final SelectableChannel channel, final Subscriber handler);
}
