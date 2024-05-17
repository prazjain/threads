package com.prash.constructs;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;

/**
 * https://www.youtube.com/watch?v=J3QZ5gfCtAg
 * Phaser is a new class that can act as CountDownLatch and CyclicBarrier both
 */
public class PhaserExample {

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(4);
        Phaser phaser = new Phaser(3);
        executor.submit(new DependentService(phaser));
        executor.submit(new DependentService(phaser));
        executor.submit(new DependentService(phaser));

        phaser.awaitAdvance(1); //similar to count down latch await();
        //for barrier, we don't anything call in phaser
        System.out.println("All dependent services initialized");
    }

    public static class DependentService implements Runnable {
        private Phaser phaser;
        public DependentService(Phaser phaser) {
            this.phaser = phaser;
        }

        @Override
        public void run() {
            //startup task
            phaser.arrive();// similar to countDown()
            //phaser.arriveAndAwaitAdvance(); // similar to barrier.await()
            //continue w/ other operation
        }
    }
}
