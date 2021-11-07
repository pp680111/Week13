package com.zst.week13.array.mq.core;

public class AmqConsumer<T> {
    private Amq<T> queue;
    private int offset;

    public AmqConsumer(Amq<T> queue) {
        this.queue = queue;
        this.offset = queue.getCurrentOffset();
    }

    public AmqMessage<T> poll() {
        AmqMessage<T> message = queue.poll(this.offset);
        if (message != null) {
            offset++;
        }
        return message;
    }

    public boolean seek(int newOffset) {
        if (queue.isValidOffset(newOffset)) {
            this.offset = newOffset;
            return true;
        } else {
            return false;
        }
    }
}
