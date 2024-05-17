package com.prash.techinterview.game;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Here, we will keep 1000 threads, one thread for each level.
 * Each thread will write date for each level.
 * There will be multiple readers reading from that data.
 * This code is when we have single instance of this code deployed (not scaled across multiple jvms)
 */
public class LeaderboardFastest {

    ConcurrentMap<Integer, Level> levels = new ConcurrentHashMap<>();
    ConcurrentMap<Integer, ExecutorService> levelwiseThreadMap = new ConcurrentHashMap<>();
    ExecutorService readerThreads = Executors.newCachedThreadPool();

    /**
     * Keep the threads ready to serve requests for each given level
     */
    public LeaderboardFastest() {
        for(int i = 1 ; i <= 1000; i++) {
            levelwiseThreadMap.put(i, Executors.newSingleThreadExecutor());
            levels.put(i, new Level());
        }
    }

    /**
     * There will always be only 1 thread that will write score for a given level.
     * @param playerId
     * @param level
     * @param score
     */
    public void updateScore(String playerId, int level, int score) {
        levelwiseThreadMap.get(level).submit(() -> {
            levels.get(level).insert(playerId, score);
        });
    }

    /**
     * High performance read method to get top5 scores for a given level
     * @param level
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public PlayerScoreDto[] top5(int level) throws ExecutionException, InterruptedException {
        return CompletableFuture.supplyAsync(() ->  levels.get(level).getDto(), readerThreads).get();
    }

    /**
     * Stores top scores for given level
     */
    public static class Level {
        //will store results in descending order of score
        private volatile PlayerScorePair[] top5 ;
        private ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
        private ReentrantReadWriteLock.WriteLock writeLock = reentrantReadWriteLock.writeLock();
        private ReentrantReadWriteLock.ReadLock readLock = reentrantReadWriteLock.readLock();

        public Level() {
            top5 = new PlayerScorePair[5];
            for(int i = 0; i < 5; i++) {
                top5[i] = new PlayerScorePair("", Integer.MIN_VALUE);
            }
        }

        /**
         * Creates clone of top scores on this level, used by readers asking for latest scores
         * @return
         */
        public PlayerScoreDto[] getDto() {
            PlayerScoreDto[] dtos = new PlayerScoreDto[5];
            readLock.lock();
            for(int i = 0; i < 5; i++) {
                dtos[i] = new PlayerScoreDto(top5[i].playerId, top5[i].score);
            }
            readLock.unlock();
            return dtos;
        }

        /**
         * Operations here will happen on single thread assigned to this level
         * Complexity is constant time (as we are looping fixed 5 times.
         * @param playerId
         * @param score
         */
        public void insert(String playerId, int score) {
            if (score < top5[4].score) {
                return;
            }
            writeLock.lock();
            for(int i = 0; i < 5; i++) {
                if (score >= top5[i].score) {
                    for(int j = 4; j > i; j--) {
                        top5[j].score = top5[j-1].score;
                        top5[j].playerId = top5[j-1].playerId;
                    }
                    top5[i].score = score;
                    top5[i].playerId = playerId;
                    break;
                }
            }
            writeLock.unlock();
        }
    }

    public static class PlayerScoreDto {
        private String playerId;
        private int score;
        public PlayerScoreDto(String playerId, int score) {
            this.playerId = playerId;
            this.score = score;
        }

        public String getPlayerId() {
            return playerId;
        }

        public int getScore() {
            return score;
        }

    }

    public static class PlayerScorePair {
        private volatile String playerId ;
        private volatile int score;
        public PlayerScorePair(String playerId, int score) {
            this.playerId = playerId;
            this.score = score;
        }
    }

}
