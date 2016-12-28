package xyz.jpmrno.niomps.dispatcher;

import java.io.IOException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;

public class SingleSelectorDispatcher implements Dispatcher, SubscriptionManager {
    private final Selector selector;

    public SingleSelectorDispatcher() throws IOException {
        this.selector = Selector.open();
    }

    @Override
    public void dispatch() throws IOException {
        if (selector.select() != 0) {
            Iterator<SelectionKey> it = selector.selectedKeys().iterator();

            while (it.hasNext()) {
                SelectionKey key = it.next();
                it.remove(); // http://stackoverflow.com/q/7132057/3349531

                handle(key);
            }
        }
    }

    private void handle(SelectionKey key) {
        if (key.isValid() && key.isAcceptable()) {
            AcceptHandler handler = (AcceptHandler) key.attachment();
            handler.accept();
        } else {
            ConnectionHandler handler = (ConnectionHandler) key.attachment();

            if (key.isValid() && key.isConnectable()) {
                handler.connect();
            }

            if (key.isValid() && key.isReadable()) {
                handler.read();
            }

            if (key.isValid() && key.isWritable()) {
                handler.write();
            }
        }
    }

    @Override
    public SubscriptionManager getManager() {
        return this;
    }

    @Override
    public void close() throws IOException {
        selector.close();
    }

    @Override
    public Subscription subscribe(SelectableChannel channel, Subscriber handler) {
        Subscription subscription;

        try {
            subscription = new Subscription(selector, channel, handler);
        }
//        catch (ClosedSelectorException exception) {
//            throw exception;
//        }
        catch (Exception exception) {
            return null;
        }

        return subscription;
    }
}