package com.deep.domain;

public class OutstandingRoll {
    private int playerId;
    private int outstandingRollCount;

    public OutstandingRoll(int playerId, int outstandingRollCount) {
        this.playerId = playerId;
        this.outstandingRollCount = outstandingRollCount;
    }

    public boolean isOngoing() {
        return outstandingRollCount != 0;
    }

    public void decrement() {
        --outstandingRollCount;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public int getOutstandingRollCount() {
        return outstandingRollCount;
    }

    public void setOutstandingRollCount(int outstandingRollCount) {
        this.outstandingRollCount = outstandingRollCount;
    }

}
