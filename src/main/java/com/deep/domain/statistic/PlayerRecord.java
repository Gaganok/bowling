package com.deep.domain.statistic;

import com.deep.domain.FrameRoll;
import com.deep.domain.Player;

import java.util.ArrayList;

public record PlayerRecord(Integer id, String stats, Integer score) implements  Comparable<PlayerRecord>{
    public static PlayerRecord of(Player player) {
        var totalScore = 0;
        var frameStats = new ArrayList<String>();

        for (FrameRoll frame : player.getScores()) {
            frameStats.add(frame.toString());
            totalScore += frame.getTotal();
        }

        var gameStat = String.join(", ", frameStats) + " total: " + totalScore;

        return new PlayerRecord(player.getId(), gameStat, totalScore);
    }

    @Override
    public int compareTo(PlayerRecord o) {
        return Integer.compare(score, o.score);
    }
}
