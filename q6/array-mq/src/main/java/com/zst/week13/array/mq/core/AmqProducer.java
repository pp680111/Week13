package com.zst.week13.array.mq.core;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AmqProducer<T> {
    private AmqBroker broker;

    public boolean send(String topic, AmqMessage<T> message) {
        Amq<T> queue = broker.findMq(topic);
        if (queue == null) {
            return false;
        }

        return queue.add(message);
    }
}
