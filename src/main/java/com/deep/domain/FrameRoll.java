package com.deep.domain;

import com.deep.domain.bonus.BonusRollType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.deep.domain.bonus.BonusRollType.OPEN;
import static com.deep.domain.bonus.BonusRollType.SPARE;
import static com.deep.domain.bonus.BonusRollType.STRIKE;

public class FrameRoll {

    List<Integer> rolls;

    int bonus;
    int total;

    public FrameRoll(Integer... firstScore) {
        this.rolls = new ArrayList<>(List.of(firstScore));
        this.total = totalRollsScore();
    }

    public boolean isComplete() {
        return rolls.size() >= 2 || total == 10;
    }

    public void addRoll(Integer rollResult) {
        this.rolls.add(rollResult);
        recalculateTotal();
    }

    public BonusRollType getBonusType() {
        if (total == 10) {
            return Objects.equals(rolls.get(0), 10) ? STRIKE : SPARE;
        }
        return OPEN;
    }

    public void setBonus(Integer bonus) {
        this.bonus = bonus;
        recalculateTotal();
    }

    public Integer getBonus() {
        return bonus;
    }

    public List<Integer> getRolls() {
        return rolls;
    }

    public Integer getTotal() {
        return total;
    }

    private Integer totalRollsScore() {
        return rolls.stream().reduce(0, Integer::sum);
    }

    private void recalculateTotal() {
        this.total = totalRollsScore() + bonus;
    }

    @Override
    public String toString() {
        return "[" + rolls.stream()
                .map(Object::toString)
                .collect(Collectors.joining(", ")) + " => " + total + "]";
    }
}
