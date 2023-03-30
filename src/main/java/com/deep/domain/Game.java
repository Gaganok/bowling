package com.deep.domain;

import java.util.ArrayList;
import java.util.List;

public class Game {

    final int id;
    int frame;
    int currentPlayerId;

    final List<Player> players;
    OutstandingRoll outstandingRoll;
    private FrameRoll ongoingRoll;

    public Game(int id) {
        this.id = id;
        players = new ArrayList<>();
        ongoingRoll = new FrameRoll();
    }

    public Player currentPlayer() {
        return players.get(currentPlayerId);
    }

    private boolean isLastPlayerInFrame() {
        return currentPlayerId == players.size() - 1;
    }

    public boolean isLastFrame() {
        return frame == 9;
    }

    public boolean isOver() {
        return frame == 10;
    }

    public boolean isPlayable() {
        return players.isEmpty();
    }

    public boolean canFinishPlayerTurn() {
        return outstandingRoll == null && ongoingRoll.isComplete();
    }

    public void nextPlayer() {
        if(isLastPlayerInFrame()) {
            ++frame;
            currentPlayerId = 0;
        } else {
            ++currentPlayerId;
        }
    }

    public void applyPlayerScore() {
        //fill in
        while(ongoingRoll.rolls.size() < 2) {
            ongoingRoll.addRoll(0);
        }

        currentPlayer().getScores().add(ongoingRoll);
        ongoingRoll = new FrameRoll();
    }

    public void setOutstandingRoll(OutstandingRoll outstandingRoll) {
        this.outstandingRoll = outstandingRoll;
    }

    public int getId() {
        return id;
    }

    public int getFrame() {
        return frame;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public OutstandingRoll getOutstandingRoll() {
        return outstandingRoll;
    }

    public void setFrame(int frame) {
        this.frame = frame;
    }

    public FrameRoll getOngoingRoll() {
        return ongoingRoll;
    }
}
