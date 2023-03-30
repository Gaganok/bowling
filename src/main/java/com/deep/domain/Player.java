package com.deep.domain;

import com.deep.domain.bonus.BonusRoll;

import java.util.ArrayList;
import java.util.List;

public class Player {
    final int id;
    final List<FrameRoll> scores;
    List<BonusRoll> bonusRolls;

    public Player(int id) {
        this.id = id;
        this.scores = new ArrayList<>(10);
        bonusRolls = new ArrayList<>();
    }

    public void completeRoll(int frame, FrameRoll roll) {
        scores.set(frame, roll);
    }

    public int getId() {
        return id;
    }

    public List<FrameRoll> getScores() {
        return scores;
    }

    public List<BonusRoll> getBonusRolls() {
        return bonusRolls;
    }
}
