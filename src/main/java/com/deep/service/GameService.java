package com.deep.service;

import com.deep.domain.Game;
import com.deep.domain.OutstandingRoll;
import com.deep.domain.Player;
import com.deep.domain.bonus.BonusRollType;
import com.deep.domain.bonus.Spare;
import com.deep.domain.bonus.Strike;
import com.deep.repository.GameRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

import static com.deep.domain.bonus.BonusRollType.OPEN;
import static com.deep.utils.UniqueIdGenerator.generateId;

@ApplicationScoped
public class GameService {

    @Inject
    private GameRepository gameRepository;

    public Game createNewGame() {
        Game game = new Game(generateId());
        return gameRepository.save(game);
    }

    public List<Game> findAll() {
        return gameRepository.findAll();
    }

    public Player addPlayer(Integer gameId) {
        var game = gameRepository.findById(gameId).orElseThrow();
        var player = new Player(game.getPlayers().size());
        game.getPlayers().add(player);
        return player;
    }

    public void registerRoll(Integer gameId, Integer rollResult) {
        var game = gameRepository.findById(gameId)
                .orElseThrow(() -> new IllegalArgumentException("Game not found"));

        if(game.isOver()) {
            throw new IllegalStateException("The game is over!");
        }

        if(game.isPlayable()) {
            throw new IllegalStateException("The game has not enough players!");
        }

        if (game.getOutstandingRoll() != null) {
            handleOutstandingRoll(game, rollResult);
        } else {
            handleRoll(game, rollResult);
        }
    }

    private void handleRoll(Game game, int rollResult) {
        game.getOngoingRoll().addRoll(rollResult);
        handleHangingBonusRolls(game, rollResult);
        reckonRollBonus(game);
        finishPlayerRoll(game);
    }

    private void finishPlayerRoll(Game game) {
        if(game.canFinishPlayerTurn()) {
            game.applyPlayerScore();
            game.nextPlayer();
        }
    }

    private void handleHangingBonusRolls(Game game, Integer rollResult) {
        var player = game.currentPlayer();
        var iterator = player.getBonusRolls().iterator();
        while (iterator.hasNext()) {
            var bonusRoll = iterator.next();
            bonusRoll.addRoll(rollResult);
            if(bonusRoll.isComplete()) {
                var playerScoreBeforeBonus = player.getScores().get(bonusRoll.getScoreId());
                playerScoreBeforeBonus.setBonus(bonusRoll.toBonusScores());
                iterator.remove();
            }
        }
    }

    private void reckonRollBonus(Game game) {
        var bonusType = game.getOngoingRoll().getBonusType();

        if(bonusType == OPEN) {
            return;
        }

        if (game.isLastFrame()) {
            game.setOutstandingRoll(
                    new OutstandingRoll(game.currentPlayer().getId(), bonusType.bonusRollsGiven));
        } else {
            addBonusRoll(game, bonusType);
        }
    }

    private void addBonusRoll(Game game, BonusRollType bonusType) {
        var bonusRoll = switch (bonusType) {
            case SPARE -> new Spare(game.currentPlayer().getScores().size());
            case STRIKE -> new Strike(game.currentPlayer().getScores().size());
            default -> throw new IllegalStateException();
        };
        game.currentPlayer().getBonusRolls().add(bonusRoll);
    }

    private void handleOutstandingRoll(Game game, int rollResult) {
        var outstandingRoll = game.getOutstandingRoll();

        game.getOngoingRoll().addRoll(rollResult);
        outstandingRoll.decrement();
        handleHangingBonusRolls(game, rollResult);

        if(!outstandingRoll.isOngoing()) {
            game.setOutstandingRoll(null);
            finishPlayerRoll(game);
        }
    }

}
