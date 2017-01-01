package xyz.jpmrno.niomps.handlers;

import xyz.jpmrno.niomps.dispatcher.Subscriber;

public interface NCHandler extends Subscriber {
    void accept();
}
