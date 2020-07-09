package com.cs525.reversi.services;

import com.cs525.reversi.models.CellValue;
import com.cs525.reversi.models.Game;
import com.cs525.reversi.models.MatrixRow;
import com.cs525.reversi.models.MovePoint;
import com.cs525.reversi.req.NewGame;

import java.util.List;

public interface GameService {

	String createNewGame(NewGame newGameForm);

	List<MovePoint> nextPossibleMoves(List<MatrixRow> rows, CellValue newCellValue);

	boolean validateMove(Game game, int row, int col, CellValue newCellValue);
}
