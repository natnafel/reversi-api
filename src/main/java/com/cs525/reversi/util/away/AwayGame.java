package com.cs525.reversi.util.away;

import com.cs525.reversi.models.Game;
import com.cs525.reversi.req.CellLocation;
import com.cs525.reversi.resp.NewGameAndMoveResp;
import com.cs525.reversi.resp.ResponseStatus;
import com.cs525.reversi.services.GameService;
import org.springframework.stereotype.Component;

@Component
public abstract class AwayGame {
    private final GameService gameService;
    public AwayGame(GameService gameService) {
        this.gameService = gameService;
    }

    public final void play(Game game, String hostName, int port, boolean makeFirstMove) {
        CellLocation opponentMove = startGame(hostName, port, makeFirstMove);
        NewGameAndMoveResp serverResponse = gameService.makeMoveForOpponent(game, opponentMove);
        while(serverResponse.getInfo().getStatus() != ResponseStatus.GAME_OVER) {
            opponentMove = makeAMove(hostName, port, serverResponse.getHomeNewPiece());
            serverResponse = gameService.makeMoveForOpponent(game, opponentMove);
        }
    }

    protected abstract CellLocation startGame(String hostName, int port, boolean makeFirstMove);
    protected abstract CellLocation makeAMove(String hostName, int port, CellLocation cellLocation);
}
