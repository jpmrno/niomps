package xyz.jpmrno.niomps.handlers;

import xyz.jpmrno.niomps.dispatcher.Subscriber;

public interface ConnectionHandler extends Subscriber {
    void connect();

    void read();

    void write();
}
