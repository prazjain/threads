package com.prash.locks;

import static java.lang.Thread.sleep;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ProducerConsumerWithBlockingQueueExample {

    public static void main(String[] args) {
        BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<>(3);
        Producer producer = new Producer(blockingQueue);
        Consumer consumer = new Consumer(blockingQueue);
        Thread producerThread = new Thread(producer);
        Thread consumerThread = new Thread(consumer);
        producerThread.start();
        consumerThread.start();
    }

    public static class Producer implements Runnable {
        private BlockingQueue<String> blockingQueue ;
        public Producer(BlockingQueue<String> q) { this.blockingQueue = q ; }
        @Override
        public void run() {
            while(true) {
                long millis = System.currentTimeMillis();
                try {
                    this.blockingQueue.put("" + millis);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static class Consumer implements Runnable {
        private BlockingQueue<String> blockingQueue ;
        public Consumer(BlockingQueue<String> q) { this.blockingQueue = q ; }
        @Override
        public void run() {
            while(true) {
                try {
                    String val = this.blockingQueue.take();
                    System.out.println("Consumed : " + val);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
