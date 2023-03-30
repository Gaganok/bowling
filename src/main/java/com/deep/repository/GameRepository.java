package com.deep.repository;

import com.deep.domain.Game;
import com.deep.domain.Player;

import java.util.List;
import java.util.Optional;

public interface GameRepository {
    Game save(Game game);
    Optional<Game> findById(Integer id);
    List<Game> findAll();
    int count();

    Optional<Player> findByIdAndGame(Integer id, Game game);
    Optional<List<Player>> findAllPlayersInGame(Game game);
}
