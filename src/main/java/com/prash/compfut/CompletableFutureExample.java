package com.prash.compfut;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CompletableFutureExample {

    /**
     * Runs the future ForkJoinPool common thread pool
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public List<Object> getData() throws ExecutionException, InterruptedException {
        CompletableFuture<List<Object>> completableFuture = CompletableFuture.supplyAsync(()-> {
            try {
                System.out.println(Thread.currentThread().getName());
                return EmployeeDb.getObjects();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        });
        return completableFuture.get();
    }

    /**
     * Give custom thread pool to run this future
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public List<Object> getDataWithNewThreadPool() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        CompletableFuture<List<Object>> completableFuture = CompletableFuture.supplyAsync(()-> {
            try {
                System.out.println(Thread.currentThread().getName());
                return EmployeeDb.getObjects();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }, executorService);
        return completableFuture.get();
    }

    public List<Object> getDataWithApply() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        CompletableFuture<List<Object>> completableFuture = CompletableFuture.supplyAsync(()-> {
            try {
                System.out.println("In getData with Apply : " + Thread.currentThread().getName());
                return EmployeeDb.getObjects();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }, executorService)
            .thenApply((list) -> {
                System.out.println("In then apply: " +  Thread.currentThread().getName());
                return list;
            })
            .thenApplyAsync((list) -> {
                System.out.println("In next then apply " + Thread.currentThread().getName());
                return list;
            });

        completableFuture
            .thenAcceptAsync((list) -> {
                System.out.println("In then accept " + Thread.currentThread().getName());
            }).thenRunAsync(()-> {
            System.out.println("In then run " + Thread.currentThread().getName());
        });

        return completableFuture.get();
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFutureExample example = new CompletableFutureExample();
        List<Object> data = example.getData();
        System.out.println(data.size());
        data = example.getDataWithNewThreadPool();
        System.out.println(data.size());
        data = example.getDataWithApply();
        System.out.println(data.size());
    }

}
