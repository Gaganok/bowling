package com.deep.domain.bonus;

public sealed interface BonusRoll permits AbstractBonusRoll {
    boolean isComplete();
    void addRoll(int rollScore);
    int getScoreId();
    int toBonusScores();
}
