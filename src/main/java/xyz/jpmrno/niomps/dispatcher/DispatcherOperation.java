package xyz.jpmrno.niomps.dispatcher;

import java.nio.channels.SelectionKey;

public enum DispatcherOperation {
    ACCEPT(SelectionKey.OP_ACCEPT),
    CONNECT(SelectionKey.OP_CONNECT),
    READ(SelectionKey.OP_READ),
    WRITE(SelectionKey.OP_WRITE),
    READWRITE(SelectionKey.OP_READ | SelectionKey.OP_WRITE),
    NONE(0x0);

    private final int value;

    DispatcherOperation(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
