package com.prash.constructs;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * https://www.youtube.com/watch?v=J3QZ5gfCtAg
 * Cyclic barriers is used for scenario when we need to block a certain set of threads until they all reach a porticular point in processing.
 * eg in games we want to send message to all users at roughly the same time, so we will use this barrier to pause each users (message sending) thread
 * and then let them all send messages.
 */
public class CyclicBarrierExample {

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        CyclicBarrier barrier = new CyclicBarrier(3);
        executorService.submit(new Task(barrier));
        executorService.submit(new Task(barrier));
        executorService.submit(new Task(barrier));

        Thread.sleep(2000);
    }

    public static class Task implements Runnable {
        private CyclicBarrier barrier;
        public Task(CyclicBarrier barrier) { this.barrier = barrier; }

        @Override
        public void run() {
            while(true) {
                try {
                    //all tasks will wait until they reach this point
                    barrier.await();
                    // then they all continue ahead
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
