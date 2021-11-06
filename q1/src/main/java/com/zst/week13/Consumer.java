package com.zst.week13;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;

@Component
public class Consumer {
    private LongAdder c1 = new LongAdder();
    private LongAdder c2 = new LongAdder();

    @PreDestroy
    public void preDestroy() {
        System.out.println("Consumer 1 receive times = " + c1.intValue());
        System.out.println("Consumer 2 receive times = " + c2.intValue());
    }

    @KafkaListener(id = "consumer1", groupId = "group1", topics = "t1")
    public void group1Consumer1(String msg) {
        System.out.println("Group1 Consumer1 receive = " + msg);
        c1.increment();
    }

    @KafkaListener(id = "consumer2", groupId = "group1", topics = "t1")
    public void group1Consumer2(String msg) {
        System.out.println("Group1 Consumer2 receive = " + msg);
        c2.increment();
    }

    @KafkaListener(groupId = "group2", topics = "t1")
    public void group2Consumer(String msg) {
        System.out.println("Group2 Consumer receive = " + msg);
    }
}
