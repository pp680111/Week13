package com.zst.week13.array.mq.core;

import lombok.SneakyThrows;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class Amq<T> {
    private static final int CAPACITY = 1024;

    private AmqMessage<T>[] queue;
    private int capacity;
    private AtomicInteger cur = new AtomicInteger(0);
    private ReentrantLock reentrantLock = new ReentrantLock(true);

    public Amq() {
        this.capacity = CAPACITY;
        queue = new AmqMessage[this.capacity];
    }

    public Amq(int capacity) {
        this.capacity = capacity;
        queue = new AmqMessage[this.capacity];
    }

    @SneakyThrows
    public boolean add(AmqMessage<T> message) {
        int currentIndex = cur.getAndIncrement();
        // 数据较多时，数组扩容的时间会比较长，这里可以优化一下
        if (currentIndex >= this.capacity) {
            reentrantLock.lock();
            try {
                if (currentIndex >= this.capacity) {
                    expand();
                }
            } catch (Exception e) {
                throw e;
            } finally {
                reentrantLock.unlock();
            }
        }

        queue[currentIndex] = message;
        return true;
    }

    public AmqMessage<T> poll(int offset) {
        if (!isValidOffset(offset)) {
            return null;
        }

        return queue[offset];
    }

    public int getCurrentOffset() {
        return this.cur.get();
    }

    public boolean isValidOffset(int offset) {
        return offset < capacity || offset >= 0;
    }

    /**
     * 对数组进行扩容
     *
     * TODO 本来想尝试实现类似于散列表的分多次扩容的处理方式，直接创建一个size*2的新数组，
     * 每次添加数据都执行一小批旧的消息数据复制到新的数组的方式实现多阶段扩容，降低单次扩容时间，
     * 不过并发时的状态控制过于复杂，之后有时间调试了再继续实现
     */
    @SneakyThrows
    private void expand() {
        AmqMessage<T>[] oldQueue = this.queue;
        AmqMessage<T>[] newQueue = new AmqMessage[this.capacity * 2];

        for(int i = 0; i < oldQueue.length; i++) {
            newQueue[i] = oldQueue[i];
        }
        this.queue = newQueue;
        this.capacity = this.capacity * 2;
    }
}
