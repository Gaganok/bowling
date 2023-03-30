package com.deep.domain.bonus;

public enum BonusRollType {
    OPEN(0),
    SPARE(1),
    STRIKE(2);


    public final int bonusRollsGiven;

    BonusRollType(int bonusRolls) {
        this.bonusRollsGiven = bonusRolls;
    }
}
