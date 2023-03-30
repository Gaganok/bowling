package com.deep.service;

import com.deep.domain.bonus.Spare;
import com.deep.domain.bonus.Strike;
import com.deep.repository.GameRepository;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.Optional;

import static com.deep.domain.DomainModelGenerator.aGame;
import static com.deep.domain.bonus.BonusRollType.SPARE;
import static com.deep.domain.bonus.BonusRollType.STRIKE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@QuarkusTest
class GameServiceTest {

    @InjectMock
    private GameRepository gameRepository;

    @Inject
    private GameService underTest;

    @Test
    void shouldCreateGame() {
        var game = aGame(1);
        when(gameRepository.save(any())).thenReturn(game);
        assertThat(underTest.createNewGame(), equalTo(game));
    }

    @Test
    void shouldAddPlayerGame() {
        var game = aGame(1);
        when(gameRepository.findById(1)).thenReturn(Optional.of(game));

        var addedPlayerA = underTest.addPlayer(1);
        var addedPlayerB = underTest.addPlayer(1);

        assertEquals(0, addedPlayerA.getId());
        assertEquals(1, addedPlayerB.getId());
    }

    @Test
    void shouldRegisterFirstRoll() {
        var game = aGame(1);
        when(gameRepository.findById(1)).thenReturn(Optional.of(game));

        underTest.addPlayer(game.getId());
        underTest.registerRoll(1, 5);

        assertEquals(5, game.getOngoingRoll().getTotal());
    }

    @Test
    void shouldRegisterSecondRoll() {
        var game = aGame(1);
        game.getOngoingRoll().addRoll(1);

        when(gameRepository.findById(1)).thenReturn(Optional.of(game));

        underTest.addPlayer(game.getId());
        underTest.registerRoll(1, 5);

        assertEquals(0, game.getOngoingRoll().getTotal());
        assertEquals(1, game.getFrame());

        var playerScores = game.getPlayers().get(0).getScores();
        assertEquals(1, playerScores.size());

        var score = playerScores.get(0);
        assertEquals(6, score.getTotal());
    }

    @Test
    void shouldRegisterStrikeRoll() {
        var game = aGame(1);
        when(gameRepository.findById(1)).thenReturn(Optional.of(game));

        underTest.addPlayer(game.getId());
        underTest.registerRoll(1, 10);

        assertEquals(0, game.getOngoingRoll().getTotal());
        assertEquals(1, game.getFrame());

        var bonusRolls = game.currentPlayer().getBonusRolls();
        assertEquals(1, bonusRolls.size());

        var bonusRoll = bonusRolls.get(0);
        assertFalse(bonusRoll.isComplete());
        assertInstanceOf(Strike.class, bonusRoll);

        var playerScores = game.getPlayers().get(0).getScores();
        assertEquals(1, playerScores.size());

        var score = playerScores.get(0);
        assertEquals(10, score.getTotal());
    }

    @Test
    void shouldRegisterStrikeBonusRolls() {
        var game = aGame(1);
        when(gameRepository.findById(1)).thenReturn(Optional.of(game));

        var player = underTest.addPlayer(game.getId());
        underTest.registerRoll(1, 10);

        var strikeScore = player.getScores().get(0);
        assertEquals(STRIKE, strikeScore.getBonusType());
        assertEquals(10, strikeScore.getTotal());
        assertEquals(0, strikeScore.getBonus());

        // Insure the same player rolling
        assertEquals(player, game.currentPlayer());
        assertEquals(1, game.getFrame());
        underTest.registerRoll(1, 4);
        underTest.registerRoll(1, 4);
        underTest.registerRoll(1, 4);

        assertEquals(2, game.getFrame());
        assertTrue(player.getBonusRolls().isEmpty());
        assertEquals(18, strikeScore.getTotal());
        assertEquals(8, strikeScore.getBonus());
    }

    @Test
    void shouldRegisterSpareRoll() {
        var game = aGame(1);
        when(gameRepository.findById(1)).thenReturn(Optional.of(game));

        var player = underTest.addPlayer(game.getId());
        underTest.registerRoll(1, 5);
        underTest.registerRoll(1, 5);

        assertEquals(1, game.getFrame());
        assertEquals(1, player.getBonusRolls().size());

        var bonusRoll = player.getBonusRolls().get(0);
        assertEquals(0, bonusRoll.getScoreId());
        assertInstanceOf(Spare.class, bonusRoll);
    }

    @Test
    void shouldRegisterSpareBonusRoll() {
        var game = aGame(1);
        when(gameRepository.findById(1)).thenReturn(Optional.of(game));

        var player = underTest.addPlayer(game.getId());
        underTest.registerRoll(1, 5);
        underTest.registerRoll(1, 5);

        var strikeScore = player.getScores().get(0);
        assertEquals(SPARE, strikeScore.getBonusType());
        assertEquals(10, strikeScore.getTotal());
        assertEquals(0, strikeScore.getBonus());

        // Insure the same player rolling
        assertEquals(player, game.currentPlayer());
        assertEquals(1, game.getFrame());
        underTest.registerRoll(1, 4);
        underTest.registerRoll(1, 4);

        assertEquals(2, game.getFrame());
        assertTrue(player.getBonusRolls().isEmpty());
        assertEquals(14, strikeScore.getTotal());
        assertEquals(4, strikeScore.getBonus());
    }

    @Test
    void failRegisterRollForCompletedGame() {
        var game = aGame(1);
        game.setFrame(10);

        when(gameRepository.findById(1)).thenReturn(Optional.of(game));
        var exception = assertThrows(IllegalStateException.class, () -> underTest.registerRoll(1, 5));
        assertEquals("The game is over!", exception.getMessage());
    }

    @Test
    void shouldRegisterLastFrameOutstandingSpikeRoll() {
        var game = aGame(1);
        game.setFrame(9);
        when(gameRepository.findById(1)).thenReturn(Optional.of(game));

        underTest.addPlayer(game.getId());
        underTest.addPlayer(game.getId());
        underTest.registerRoll(1, 10);

        assertEquals(9, game.getFrame());
        assertNotNull(game.getOutstandingRoll());
        assertEquals(2, game.getOutstandingRoll().getOutstandingRollCount());
        assertEquals(game.currentPlayer().getId(), game.getOutstandingRoll().getPlayerId());
    }

    @Test
    void shouldRegisterLastFrameOutstandingSpareRoll() {
        var game = aGame(1);
        game.setFrame(9);
        when(gameRepository.findById(1)).thenReturn(Optional.of(game));

        underTest.addPlayer(game.getId());
        underTest.addPlayer(game.getId());
        underTest.registerRoll(1, 5);
        underTest.registerRoll(1, 5);

        assertEquals(9, game.getFrame());
        assertNotNull(game.getOutstandingRoll());
        assertEquals(1, game.getOutstandingRoll().getOutstandingRollCount());
        assertEquals(game.currentPlayer().getId(), game.getOutstandingRoll().getPlayerId());
    }

    @Test
    void shouldRegisterOutstandingRoll() {
        var game = aGame(1);
        game.setFrame(9);
        when(gameRepository.findById(1)).thenReturn(Optional.of(game));

        var playerUnderTest = underTest.addPlayer(game.getId());
        underTest.addPlayer(game.getId());
        underTest.registerRoll(1, 10);

        var outstandingRoll = game.getOutstandingRoll();
        assertEquals(2, outstandingRoll.getOutstandingRollCount());

        underTest.registerRoll(1, 10);
        assertEquals(1, outstandingRoll.getOutstandingRollCount());

        underTest.registerRoll(1, 10);
        assertNull(game.getOutstandingRoll());

        var playUnderTestScore = playerUnderTest.getScores().get(0);
        assertEquals(0, playUnderTestScore.getBonus());
        assertEquals(30, playUnderTestScore.getTotal());
    }

}
