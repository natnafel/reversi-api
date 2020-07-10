package com.cs525.reversi.services;

import com.cs525.reversi.models.*;
import com.cs525.reversi.req.CellLocation;
import com.cs525.reversi.req.NewGame;
import com.cs525.reversi.resp.NewGameAndMoveResp;

import java.util.List;

public interface GameService {

	NewGameAndMoveResp createNewGame(NewGame newGameForm);

	List<MoveScore> nextPossibleMoves(List<MatrixRow> rows, CellValue newCellValue);

	boolean validateMove(Game game, CellLocation cellLocation, CellValue newCellValue);
}
