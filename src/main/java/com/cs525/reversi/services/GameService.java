package com.cs525.reversi.services;

import java.util.List;
import java.util.UUID;

import com.cs525.reversi.models.CellValue;
import com.cs525.reversi.models.Game;
import com.cs525.reversi.models.MatrixRow;
import com.cs525.reversi.models.MovePoint;
import com.cs525.reversi.req.NewGame;
import com.cs525.reversi.resp.Dto;
import com.cs525.reversi.resp.MoveResponse;

import java.util.List;

public interface GameService {

	String createNewGame(NewGame newGameForm);
	List<Dto> getMoves(UUID gameuuid);

	List<MovePoint> nextPossibleMoves(List<MatrixRow> rows, CellValue newCellValue);

	boolean validateMove(Game game, int row, int col, CellValue newCellValue);
}
