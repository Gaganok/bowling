package com.deep.component;

import com.deep.domain.Game;
import com.deep.domain.statistic.PlayerRecord;
import com.deep.service.GameService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Random;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@ApplicationScoped
public class BowlingGameRunner {

    @Inject
    private GameService gameService;

    private static final Random random = new Random();

    public String runGame(Integer numberOfPlayer) {
        var game = gameService.createNewGame();

        IntStream.range(0, numberOfPlayer)
                .forEach(i -> gameService.addPlayer(game.getId()));

        while (!game.isOver()) {
            rollFrame(game);
            handleOutstandingRolls(game);
        }

        return stringifyGameStat(game);
    }

    private String stringifyGameStat(Game game) {
        var playerRecords = game.getPlayers().stream()
                .map(PlayerRecord::of)
                .toList();

        var winners = "Winners = [" + playerRecords.stream()
                .collect(groupingBy(Function.identity(), TreeMap::new, toList()))
                .lastEntry()
                .getValue().stream()
                .map(winner -> winner.id() + " : " + winner.score())
                .collect(Collectors.joining(", ")) + "]";

        return playerRecords.stream()
                .map(PlayerRecord::stats)
                .collect(Collectors.joining("\n")) + "\n" + winners;
    }

    private void rollFrame(Game game) {
        var leftOver = roll(game, 10);
        if(leftOver > 0) {
            roll(game, leftOver);
        }
    }

    private int roll(Game game, int tinLeftOver) {
        var rollResult = random.nextInt(tinLeftOver + 1);
        gameService.registerRoll(game.getId(), rollResult);
        return tinLeftOver - rollResult;
    }

    private void handleOutstandingRolls(Game game) {
        int tins = 10;
        while (game.getOutstandingRoll() != null) {
            tins -= roll(game, tins);
        }
    }
}
