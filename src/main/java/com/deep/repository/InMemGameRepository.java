package com.deep.repository;

import com.deep.domain.Game;
import com.deep.domain.Player;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class InMemGameRepository implements GameRepository {

    private static final Map<Integer, Game> gameBoard = new ConcurrentHashMap<>();

    @Override
    public Game save(Game game) {
        gameBoard.put(game.getId(), game);
        return game;
    }

    @Override
    public Optional<Game> findById(Integer id) {
        return Optional.ofNullable(gameBoard.get(id));
    }

    @Override
    public List<Game> findAll() {
        return gameBoard.values().stream().toList();
    }

    @Override
    public int count() {
        return gameBoard.size();
    }

    @Override
    public Optional<Player> findByIdAndGame(Integer playerId, Game game) {
        return findAllPlayersInGame(game)
                .map(collection -> collection.get(playerId));
    }

    @Override
    public Optional<List<Player>> findAllPlayersInGame(Game game) {
        return findById(game.getId()).map(Game::getPlayers);
    }
}
