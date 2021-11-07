package com.zst.week13.array.mq;

import com.zst.week13.array.mq.core.AmqBroker;
import com.zst.week13.array.mq.core.AmqConsumer;
import com.zst.week13.array.mq.core.AmqMessage;
import com.zst.week13.array.mq.core.AmqProducer;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class App {
    public static void main(String[] args) throws InterruptedException {
        AmqBroker broker = new AmqBroker();
        broker.createTopic("t1");
        AmqConsumer<String> consumer = broker.createConsumer("t1");
        AmqProducer<String> producer = broker.createProducer();

        ExecutorService threadPool = Executors.newFixedThreadPool(12);
        CountDownLatch cdl = new CountDownLatch(4);
        for (int i = 0; i < 4; i++) {
            threadPool.execute(() -> {
                for (int j = 0; j < 1000; j++) {
                    producer.send("t1", AmqMessage.<String>builder().body(String.valueOf(j)).build());
                }
                cdl.countDown();
            });
        }
        cdl.await();
        for (int i = 0; i < 4000; i++) {
            if (consumer.poll() == null) {
                throw new RuntimeException(String.valueOf(i));
            }
        }
        threadPool.shutdown();
    }
}
