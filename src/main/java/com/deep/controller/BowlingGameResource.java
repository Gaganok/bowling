package com.deep.controller;

import com.deep.component.BowlingGameRunner;
import com.deep.domain.Game;
import com.deep.domain.Player;
import com.deep.service.GameService;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/api/v1")
public class BowlingGameResource {

    @Inject
    private GameService gameService;

    @Inject
    private BowlingGameRunner gameRunner;

    @POST
    @Path("/game")
    @Produces(MediaType.APPLICATION_JSON)
    @Tag(
            name = "Create Bowling Game",
            description = "Create a new bowling game and returns a game json"
    )
    public Game createGame() {
        return gameService.createNewGame();
    }

    @GET
    @Path("/game")
    @Produces(MediaType.APPLICATION_JSON)
    @Tag(
            name = "Get all created bowling games",
            description = "Returns a list of all created bowling games"
    )
    public List<Game> getAllGames() {
        return gameService.findAll();
    }

    @POST
    @Path("/player")
    @Produces(MediaType.APPLICATION_JSON)
    @Tag(
            name = "Add a player to the existing game",
            description = "Create and add a new player to the game corresponding to the specified game id"
    )
    public Player addPlayer(@NotNull @QueryParam("game") Integer gameId) {
        return gameService.addPlayer(gameId);
    }

    @POST
    @Path("/roll")
    @Produces(MediaType.APPLICATION_JSON)
    @Tag(
            name = "Register roll rsult",
            description = "Register roll result for the ongoing game"
    )
    public Response makeRoll(@NotNull @QueryParam("game") Integer gameId,
                             @NotNull @QueryParam("roll") Integer rollResult) {
        gameService.registerRoll(gameId, rollResult);
        return Response.ok().build();
    }

    @POST
    @Path("/simulate")
    @Produces(MediaType.APPLICATION_JSON)
    @Tag(
            name = "Simulates bowling game with N players",
            description = """
                    Simulates a bowling game with a provided number of players.
                    Returns complete game statistics as a response.
                """
    )
    public String simulate(@DefaultValue("1") @QueryParam("players") Integer numberOfPlayer) {
        return gameRunner.runGame(numberOfPlayer);
    }
}