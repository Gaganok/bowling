package com.deep.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GameTest {

    @ParameterizedTest
    @CsvSource({"1,false", "2,false", "9,true", "10,false"})
    void isLastFrameTest(int frame, boolean result) {
        Game game = new Game(0);
        game.setFrame(frame);
        assertEquals(result, game.isLastFrame());
    }

    @ParameterizedTest
    @CsvSource({"1,false", "2,false", "9,false", "10,true"})
    void isOverTest(int frame, boolean result) {
        Game game = new Game(0);
        game.setFrame(frame);
        assertEquals(result, game.isOver());
    }

    @Test
    void shouldMoveToNextPlayer() {
        Game game = new Game(0);
        var players = List.of(
          new Player(0),
          new Player(1)
        );

        game.getPlayers().addAll(players);

        assertEquals(0, game.getFrame());
        assertEquals(0, game.currentPlayer().getId());

        game.nextPlayer();

        assertEquals(0, game.getFrame());
        assertEquals(1, game.currentPlayer().getId());
    }

    @Test
    void shouldMoveToNextFrame() {
        Game game = new Game(0);
        var players = List.of(
                new Player(0),
                new Player(1)
        );

        game.getPlayers().addAll(players);

        assertEquals(0, game.getFrame());
        assertEquals(0, game.currentPlayer().getId());

        game.nextPlayer();
        game.nextPlayer();

        assertEquals(1, game.getFrame());
        assertEquals(0, game.currentPlayer().getId());
    }

    @Test
    void testApplyPlayerScore() {
        var game = new Game(0);
        var player = new Player(0);
        var playerScores = player.getScores();

        game.getPlayers().add(player);

        assertEquals(0, playerScores.size());
        game.applyPlayerScore();
        assertEquals(1, playerScores.size());

        var scores = playerScores.get(0);
        assertEquals(2, scores.getRolls().size());
        assertEquals(0, scores.getTotal());
    }
}
