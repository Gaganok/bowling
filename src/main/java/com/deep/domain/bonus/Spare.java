package com.deep.domain.bonus;

import static com.deep.domain.bonus.BonusRollType.SPARE;

public final class Spare extends AbstractBonusRoll {
    public Spare(int scoreId) {
        super(SPARE, scoreId);
    }
}
