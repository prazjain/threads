package com.prash.techinterview.game;

import com.prash.techinterview.game.LeaderboardFastest.PlayerScoreDto;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

/**
 * We have a massively multi player game, and we maintain 2 methods,
 * 1. Update score (playerId, level, score)
 * 2. get Top 5 scorers for a given level
 * There are millions of players, number of levels is upto 1000, max score is 10,000
 */
public class LeaderboardExample {

    public static void main(String[] args) {
        LeaderboardFastest leaderboardFastest = new LeaderboardFastest();

        Set<String> guidRepeat = new HashSet<>();

        for(int i = 0; i < 10000; i++) {
            String guid = UUID.randomUUID().toString();
            if (guidRepeat.contains(guid)) {
                System.out.println("Guid repeated " + guid);
            } else {
                guidRepeat.add(guid);
            }
            leaderboardFastest.updateScore(guid, 1, (new Random()).nextInt(10000) + 1);

            if (i % 1000 == 0) {
                try {
                    PlayerScoreDto[] playerScoreDtos = leaderboardFastest.top5(1);
                    Arrays.stream(playerScoreDtos).forEach(x -> System.out.println(x.getPlayerId() + ":" + x.getScore()));
                    System.out.println("--------------------");
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
