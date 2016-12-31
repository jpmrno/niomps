package xyz.jpmrno.niomps.dispatcher;

import java.io.Closeable;
import java.io.IOException;

public interface Dispatcher extends SubscriptionManager, Closeable, AutoCloseable {
    void dispatch() throws IOException;
}
