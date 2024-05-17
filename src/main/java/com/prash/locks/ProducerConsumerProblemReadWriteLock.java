package com.prash.locks;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

// lock condition class https://www.youtube.com/watch?v=N0mMm5PF5Ow

// read write lock : https://www.youtube.com/watch?v=7VqWkc9o7RM

// multiple readers threads allowed at a time
// single write thread allowed at a time

public class ProducerConsumerProblemReadWriteLock {
    public ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    public ReentrantReadWriteLock.ReadLock readLock = lock.readLock();
    public ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();
    public Condition producers = writeLock.newCondition();
    public Condition consumers = readLock.newCondition();

    public AtomicInteger count = new AtomicInteger();

    public void produce() throws InterruptedException {
        try {
            writeLock.lock();
            while (count.get()==Integer.MAX_VALUE) {
                producers.await();
            }
            count.incrementAndGet();
            consumers.signalAll();
        } finally {
            writeLock.unlock();
        }
    }

    public String consume() throws InterruptedException {
        try {
            readLock.lock();
            while (count.get()==0) {
                consumers.await();
            }
            count.decrementAndGet();
            producers.signalAll();
        } finally {
            readLock.unlock();
        }
        return "";
    }
}
