package xyz.jpmrno.niomps.handlers;

import xyz.jpmrno.niomps.dispatcher.Subscriber;

public interface NewConnectionHandler extends Subscriber {
    void accept();
}
