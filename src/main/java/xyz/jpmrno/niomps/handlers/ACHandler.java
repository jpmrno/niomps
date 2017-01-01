package xyz.jpmrno.niomps.handlers;

import xyz.jpmrno.niomps.dispatcher.Subscriber;

public interface ACHandler extends Subscriber {
    default void connect() {
        throw new UnsupportedOperationException();
    }

    void read();

    void write();
}
