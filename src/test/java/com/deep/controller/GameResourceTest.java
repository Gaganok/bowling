package com.deep.controller;

import com.deep.repository.GameRepository;
import com.deep.service.GameService;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.deep.domain.DomainModelGenerator.aGame;
import static com.deep.domain.DomainModelGenerator.aPlayer;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;

@QuarkusTest
@TestHTTPEndpoint(BowlingGameResource.class)
class GameResourceTest {

    @InjectMock
    private GameRepository gameRepository;

    @InjectMock
    private GameService gameService;

    @Test
    void shouldReturnAllGames() {
        when(gameService.findAll()).thenReturn(List.of(aGame(1), aGame(2), aGame(3)));
        given()
                .when().get("/game")
                .then()
                .statusCode(200)
                .body("$", Matchers.hasSize(3));
    }

    @Test
    void shouldCreateGame() {
        when(gameService.createNewGame()).thenReturn(aGame(1));
        given()
                .when().post("/game")
                .then()
                .statusCode(200)
                .body("id", equalTo(1));
    }

    @Test
    void shouldAddPlayer() {
        when(gameService.addPlayer(1)).thenReturn(aPlayer(2));
        given()
                .queryParam("game", 1)
                .when().post("/player")
                .then()
                .statusCode(200)
                .body("id", equalTo(2));
    }

    @Test
    void shouldRegisterRoll() {
        given()
                .queryParam("game", 1)
                .queryParam("roll", 5)
                .when().post("/roll")
                .then()
                .statusCode(200);
    }

}