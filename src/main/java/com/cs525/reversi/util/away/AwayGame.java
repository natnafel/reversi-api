package com.cs525.reversi.util.away;

import com.cs525.reversi.models.*;
import com.cs525.reversi.req.CellLocation;
import com.cs525.reversi.services.GameService;
import com.cs525.reversi.util.Pair;

public abstract class AwayGame<T> {
    private final GameService gameService;

    public AwayGame(GameService gameService) {
        this.gameService = gameService;
    }

    public final void play(Game game, String hostName, int port, Algorithm algorithm, boolean makeFirstMove) {
        Pair<T, CellLocation> startGameResponse = startGame(hostName, port, makeFirstMove);
        if (startGameResponse.getValue() != null){
            gameService.makeMoveOnlyForOpponent(game, startGameResponse.getValue());
        }
        while(game.getStatus() == GameStatus.OPEN) {
            MoveScore serverMoveScore = gameService.makeMoveForServer(game);
            if (game.getStatus() != GameStatus.OPEN) break;
            CellLocation opponentCellMove = makeAMove(hostName, port, startGameResponse.getKey(), serverMoveScore.getCellLocation());
            gameService.makeMoveOnlyForOpponent(game, opponentCellMove);
        }
    }

    protected abstract Pair<T, CellLocation> startGame(String hostName, int port, boolean makeFirstMove);
    protected abstract CellLocation makeAMove(String hostName, int port, T identifier, CellLocation cellLocation);
    public abstract Protocol getProtocol();


}
