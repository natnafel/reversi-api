package com.cs525.reversi.models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ReversiGameBuilder implements GameBuilder {

	private Game reversiGame = null;

	public ReversiGameBuilder() {
		this.reversiGame = new Game();
	}

	@Override
	public ReversiGameBuilder buildPlayerOne(User playerOne) {
		this.reversiGame.setPlayer1(playerOne);
		return this;

	}

	@Override
	public ReversiGameBuilder buildPlayerTwo(User playerTwo) {
		this.reversiGame.setPlayer2(playerTwo);
		return this;


	}
    @Override
	 public ReversiGameBuilder buildGameStatus(GameStatus gameStatus) {
		 this.reversiGame.setStatus(gameStatus);
		 return this;
	 }
    @Override
	 public ReversiGameBuilder buildGameUUID() {
		 this.reversiGame.setUuid(UUID.randomUUID());
		 return this;
	 }
    @Override

	 public ReversiGameBuilder buildBoardGame() {
		 this.reversiGame.setRows(generateBoard());
		 this.reversiGame.setDefaultCells();
		 return this;
	 }
    
	 private List<MatrixRow> generateBoard() {
			List<MatrixRow> rows = new ArrayList<MatrixRow>();

			for (int i = 1; i <= 8; i++) {
				MatrixRow row = new MatrixRow();
				List<CellValue> cells = new ArrayList<>();
				for (int j = 1; j <= 8; j++) {
						cells.add(CellValue.EMPTY);
				}
				row.setCells(cells);
				rows.add(row);
			}
			return rows;
		}
	@Override
	public Game getGame() {
		return reversiGame;
	}

}
