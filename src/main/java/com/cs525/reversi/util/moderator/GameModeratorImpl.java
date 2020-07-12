package com.cs525.reversi.util.moderator;

import com.cs525.reversi.models.*;
import com.cs525.reversi.req.CellLocation;
import com.cs525.reversi.util.Pair;
import com.cs525.reversi.util.iterators.*;
import com.cs525.reversi.util.rules.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GameModeratorImpl implements GameModerator {

	private final Rule gameRule;

	@Value("${reversi.default-player.is-black}")
	private boolean defaultPlayerIsBlack;

	public GameModeratorImpl(EmptyRule emptyRule, OpenGameRule openGameRule, NewValueNotEmptyRule newValueNotEmptyRule,
			MustPlayTurnRule mustPlayTurnRule, ResultsInPointsRule resultsInPointsRule) {
		gameRule = emptyRule;
		emptyRule.setNext(openGameRule);
		openGameRule.setNext(newValueNotEmptyRule);
		newValueNotEmptyRule.setNext(resultsInPointsRule);
		resultsInPointsRule.setNext(mustPlayTurnRule);
	}

	@Override
	public int playerScore(Game game, User player) {
		int score = 0;
		int rowSize = game.getRows().size();
		int colSize = game.getRows().get(0).getCells().size();
		CellValue playerCellValue = getPlayerCellValue(game, player);
		for (int row = 0; row < rowSize; row++) {
			for (int col = 0; col < colSize; col++) {
				if (playerCellValue == getCellValue(game.getRows(), row, col))
					score++;
			}
		}
		return score;
	}

	@Override
	public void applyMove(Game game, MoveScore moveScore) {
		game.changeCellValue(moveScore.getCellLocation().getRow(), moveScore.getCellLocation().getCol(),
				moveScore.getNewCellValue());

		for (CellLocation flip : moveScore.getCellsToFlip()) {
			game.changeCellValue(flip.getRow(), flip.getCol(), moveScore.getNewCellValue());
		}
		if (isBoardFull(game.getRows())) {
			game.setStatus(GameStatus.CLOSED);
			// if game is a tie then winner will remain null
			if (playerScore(game, game.getPlayer1()) > playerScore(game, game.getPlayer2())) {
				game.setWinner(Player.PLAYER1);
			} else if (playerScore(game, game.getPlayer1()) < playerScore(game, game.getPlayer2())) {
				game.setWinner(Player.PLAYER2);
			}
		}
	}

	@Override
	public boolean validateMove(Game game, CellLocation cellLocation, User player) {
		return gameRule.isValid(game, cellLocation, getPlayerCellValue(game, player));
	}

	@Override
	public MoveScore moveByAlgorithmForUser(Game game, User serverUser, Algorithm algorithm) {
		return algorithm.decideMove(nextPossibleMoves(game.getRows(), getPlayerCellValue(game, serverUser)),
				game.getRows());
	}

	@Override
	public List<MoveScore> nextPossibleMoves(List<MatrixRow> rows, CellValue newCellValue) {
		List<MoveScore> result = new ArrayList<>();

		List<CellIterator> iterators = cellIterators(rows);
		int rowSize = rows.size();
		int colSize = rows.get(0).getCells().size();

		for (int row = 0; row < rowSize; row++) {
			for (int col = 0; col < colSize; col++) {
				CellLocation newCellLocation = new CellLocation(row, col);
				MoveScore moveScore = moveScoreForNewPiece(iterators, newCellLocation, newCellValue);
				if (moveScore != null) {
					result.add(moveScore);
				}
			}
		}

		return result;
	}

	@Override
	public MoveScore moveScoreForNewPiece(Game game, CellLocation newCellLocation, User player) {
		return moveScoreForNewPiece(cellIterators(game.getRows()), newCellLocation, getPlayerCellValue(game, player));
	}

	private MoveScore moveScoreForNewPiece(List<CellIterator> iterators, CellLocation newCellLocation,
			CellValue newCellValue) {
		List<CellLocation> cellsToFlip = new ArrayList<>();
		for (CellIterator iterator : iterators) {
			cellsToFlip.addAll(flipLocations(iterator, newCellLocation, newCellValue));
		}
		if (cellsToFlip.size() > 0) {
			return new MoveScore(newCellValue, newCellLocation, cellsToFlip);
		}
		return null;
	}

	private List<CellIterator> cellIterators(List<MatrixRow> rows) {
		List<CellIterator> iterators = new ArrayList<>();
		iterators.add(new NorthIterator(rows));
		iterators.add(new SouthIterator(rows));
		iterators.add(new WestIterator(rows));
		iterators.add(new EastIterator(rows));
		iterators.add(new NorthEastIterator(rows));
		iterators.add(new SouthEastIterator(rows));
		iterators.add(new SouthWestIterator(rows));
		iterators.add(new NorthWestIterator(rows));
		return iterators;
	}

	private List<CellLocation> flipLocations(CellIterator iterator, CellLocation startCell, CellValue newCellValue) {
		List<CellLocation> flipLocations = new ArrayList<>();
		boolean seenCellWithSameValue = false;

		iterator.setPosition(startCell.getRow(), startCell.getCol());

		if (iterator.isCurrentOccupied())
			return new ArrayList<>();

		while (iterator.hasNext()) {
			Pair<CellLocation, CellValue> next = iterator.next();
			if (next.getValue() == CellValue.EMPTY)
				break;
			if (next.getValue() == newCellValue) {
				seenCellWithSameValue = true;
				break;
			}
			flipLocations.add(next.getKey());
		}

		return seenCellWithSameValue ? flipLocations : new ArrayList<>();
	}

	private CellValue getPlayerCellValue(Game game, User player) {

		boolean isDefaultPlayer = game.getPlayer1().getUsername().equals(player.getUsername());
		return (defaultPlayerIsBlack && isDefaultPlayer) || (!defaultPlayerIsBlack && !isDefaultPlayer)
				? CellValue.BLACK
				: CellValue.WHITE;
	}

	private CellValue getCellValue(List<MatrixRow> rows, int row, int col) {
		return rows.get(row).getCells().get(col).getCellValue();
	}

	public boolean isBoardFull(List<MatrixRow> rows) {
		int rowSize = rows.size();
		int colSize = rows.get(0).getCells().size();
		for (int row = 0; row < rowSize; row++) {
			for (int col = 0; col < colSize; col++) {
				if (getCellValue(rows, row, col) == CellValue.EMPTY)
					return false;
			}
		}
		return true;
	}
}
