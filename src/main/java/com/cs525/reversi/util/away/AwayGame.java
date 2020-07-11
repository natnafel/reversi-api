package com.cs525.reversi.util.away;

import com.cs525.reversi.models.Algorithm;
import com.cs525.reversi.models.Game;
import com.cs525.reversi.models.MoveScore;
import com.cs525.reversi.models.Protocol;
import com.cs525.reversi.req.CellLocation;
import com.cs525.reversi.resp.Info;
import com.cs525.reversi.resp.NewGameAndMoveResp;
import com.cs525.reversi.resp.ResponseStatus;
import com.cs525.reversi.services.GameService;
import com.cs525.reversi.util.Pair;
import com.cs525.reversi.util.moderator.GameModerator;

public abstract class AwayGame<T> {
    private final GameService gameService;
    private final GameModerator gameModerator;

    public AwayGame(GameService gameService, GameModerator gameModerator) {
        this.gameService = gameService;
        this.gameModerator = gameModerator;
    }

    public final void play(Game game, String hostName, int port, Algorithm algorithm, boolean makeFirstMove) {
        Pair<T, CellLocation> startGameResponse = startGame(hostName, port, makeFirstMove);
        CellLocation opponentMove = startGameResponse.getValue();
        NewGameAndMoveResp serverResponse;
        if (opponentMove != null) {
            serverResponse = gameService.makeMoveForOpponent(game, opponentMove);
        } else {
            MoveScore serverMove = gameModerator.moveByAlgorithmForUser(game, gameService.getDefaultPlayer(), algorithm);
            serverResponse = new NewGameAndMoveResp();
            serverResponse.setHomeNewPiece(serverMove.getCellLocation());
            serverResponse.setInfo(new Info(ResponseStatus.SUCCESSFUL, ""));
        }
        while(serverResponse == null || serverResponse.getInfo().getStatus() != ResponseStatus.GAME_OVER) {
            opponentMove = makeAMove(hostName, port, startGameResponse.getKey(), serverResponse.getHomeNewPiece());
            serverResponse = gameService.makeMoveForOpponent(game, opponentMove);
        }
    }

    protected abstract Pair<T, CellLocation> startGame(String hostName, int port, boolean makeFirstMove);
    protected abstract CellLocation makeAMove(String hostName, int port, T identifier, CellLocation cellLocation);
    public abstract Protocol getProtocol();
}
