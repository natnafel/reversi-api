package com.cs525.reversi.services;

import java.util.List;
import java.util.UUID;

import com.cs525.reversi.models.CellValue;
import com.cs525.reversi.models.Game;
import com.cs525.reversi.models.LookupResp;
import com.cs525.reversi.models.MatrixRow;
import com.cs525.reversi.models.MoveScore;
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

	NewGameAndMoveResp makeMoveForClient(UUID gameUUID, CellLocation newMoveLocation);
}
