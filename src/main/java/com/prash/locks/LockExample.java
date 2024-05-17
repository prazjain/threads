package com.prash.locks;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

// java locks : https://www.youtube.com/watch?v=ahBC69_iyk4
// java lock condition : https://www.youtube.com/watch?v=N0mMm5PF5Ow
public class LockExample {

    //non-fifo lock queue, but can cause starvation and s not fair
    public ReentrantLock reentrantLock = new ReentrantLock();
    //fifo lock queue and will not cause starvation
    public ReentrantLock reentrantLockFair = new ReentrantLock(true);

    public Condition reentrantLockCondition = reentrantLock.newCondition();

    public void testUsingLock() {

        try {
            reentrantLock.lock();

            //if condition not met
            reentrantLockCondition.await();

            reentrantLockCondition.signalAll();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            reentrantLock.unlock();
        }

    }
}
