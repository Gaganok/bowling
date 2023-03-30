package com.deep.domain.bonus;

import static com.deep.domain.bonus.BonusRollType.STRIKE;

public final class Strike extends AbstractBonusRoll {
    public Strike(int scoreId) {
        super(STRIKE, scoreId);
    }
}
