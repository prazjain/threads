package com.prash.locks;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

// lock condition class https://www.youtube.com/watch?v=N0mMm5PF5Ow

// read write lock : https://www.youtube.com/watch?v=7VqWkc9o7RM

public class ProducerConsumerProblemReentrantLock {
    public ReentrantLock reentrantLock = new ReentrantLock();
    public Condition producers = reentrantLock.newCondition();
    public Condition consumers = reentrantLock.newCondition();
    public AtomicInteger count = new AtomicInteger();

    public void produce() throws InterruptedException {
        try {
            reentrantLock.lock();
            while (count.get()==Integer.MAX_VALUE) {
                producers.await();
            }
            count.incrementAndGet();
            consumers.signalAll();
        } finally {
            reentrantLock.unlock();
        }
    }

    public String consume() throws InterruptedException {
        try {
            reentrantLock.lock();
            while (count.get()==0) {
                consumers.await();
            }
            count.decrementAndGet();
            producers.signalAll();
        } finally {
            reentrantLock.unlock();
        }
        return "";
    }
}
