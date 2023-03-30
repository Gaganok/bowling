package com.deep.domain.bonus;

import java.util.Arrays;

public sealed class AbstractBonusRoll implements BonusRoll permits Spare, Strike {

    protected final BonusRollType type;
    private final int[] bonus;
    private int bonusRollsCounted;
    private final int scoreId;

    public AbstractBonusRoll(BonusRollType type, int scoreId) {
        this.bonus = new int[type.bonusRollsGiven];
        this.type = type;
        this.scoreId = scoreId;
    }

    @Override
    public boolean isComplete() {
        return bonusRollsCounted >= type.bonusRollsGiven;
    }

    @Override
    public void addRoll(int rollScore) {
        if(isComplete()) {
            throw new IllegalArgumentException("No more bonus rolls permitted");
        }

        bonus[bonusRollsCounted++] = rollScore;
    }

    @Override
    public int getScoreId() {
        return scoreId;
    }

    @Override
    public int toBonusScores() {
        return Arrays.stream(bonus).sum();
    }
}
