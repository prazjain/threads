package com.prash.constructs;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * https://www.youtube.com/watch?v=J3QZ5gfCtAg
 * countdown latch is used a mechanism to block a thread until other threads have reached a particular point.
 * the other threads will not block and will continue ahead after giving notification that they have reached a particular point
 */
public class CountDownLatchExample {

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(4);

        // this latch will count down from 3 to 0
        CountDownLatch latch = new CountDownLatch(3);
        executorService.submit(new DependentService(latch));
        executorService.submit(new DependentService(latch));
        executorService.submit(new DependentService(latch));

        latch.await();
        System.out.println("All dependent services initialized");
        //program initialized, perform other operations

    }

    public static class DependentService implements Runnable {
        private CountDownLatch latch ;
        public DependentService(CountDownLatch latch) { this.latch = latch ; }

        @Override
        public void run() {
            //startup task
            latch.countDown();
            //continue w/ other operations
        }
    }
}
