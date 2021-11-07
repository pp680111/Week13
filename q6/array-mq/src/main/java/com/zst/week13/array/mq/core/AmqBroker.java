package com.zst.week13.array.mq.core;

import com.zst.week13.array.mq.core.exceptions.QueueNotExistException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AmqBroker {
    private final Map<String, Amq> amqMap = new ConcurrentHashMap<>(64);

    public void createTopic(String name) {
        amqMap.putIfAbsent(name, new Amq());
    }

    public Amq findMq(String topic) {
        return amqMap.get(topic);
    }

    public <T> AmqProducer<T> createProducer() {
        return new AmqProducer(this);
    }

    public <T> AmqConsumer<T> createConsumer(String topic) {
        if (!amqMap.containsKey(topic)) {
            throw new QueueNotExistException();
        }
        return new AmqConsumer(amqMap.get(topic));
    }
}
