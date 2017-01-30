package xyz.jpmrno.niomps.protocol.echo;

import xyz.jpmrno.niomps.protocol.ProtocolContext;
import xyz.jpmrno.niomps.protocol.ProtocolState;

import java.nio.ByteBuffer;

public class EchoServerPS implements ProtocolState {
    @Override
    public void afterAccept(ProtocolContext context) {
        context.getConnection().requestRead();
    }

    @Override
    public void afterRead(ProtocolContext context, ByteBuffer source) {
        ByteBuffer buffer = context.getBuffer();

        buffer.clear();
        buffer.put(source);
        buffer.flip();
        context.getConnection().requestWrite(buffer);
    }

    @Override
    public void afterWrite(ProtocolContext context) {
        ByteBuffer buffer = context.getBuffer();

        if (buffer.hasRemaining()) {
            context.getConnection().requestWrite(buffer);
        } else {
            context.getConnection().requestRead();
        }
    }

    @Override
    public void afterClose(ProtocolContext context) {
        System.out.println("[~] Connection closed");
    }
}
