package com.cs525.reversi.services;

import java.util.List;
import java.util.UUID;

import com.cs525.reversi.models.*;
import com.cs525.reversi.req.AwayGameRequest;
import com.cs525.reversi.req.CellLocation;
import com.cs525.reversi.req.NewGame;
import com.cs525.reversi.resp.AwayGameResponse;
import com.cs525.reversi.resp.GameResponse;
import com.cs525.reversi.resp.MoveResponse;
import com.cs525.reversi.resp.NewGameAndMoveResp;


public interface GameService {

	List<MoveResponse> getMoves(UUID gameuuid);

	NewGameAndMoveResp createNewGame(NewGame newGameForm);

	GameResponse getGame(UUID uuid);

	List<LookupResp> getSupportedAlgorithms();

    AwayGameResponse startAwayGame(AwayGameRequest awayGameRequest);

	NewGameAndMoveResp makeMoveForOpponent(UUID gameUUID, CellLocation newMoveLocation);

	NewGameAndMoveResp makeMoveForOpponent(Game game, CellLocation newMoveLocation);

	User getDefaultPlayer();

    List<LookupResp> getSupportedProtocols();

	List<GameResponse> getAllGames();
}
