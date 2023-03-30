package com.deep.domain;

public class DomainModelGenerator {

    public static Game aGame(Integer id) {
        return new Game(id);
    }
    public static Player aPlayer(Integer id) {
        return new Player(id);
    }
}
