package com.cs525.reversi.models;

import java.util.ArrayList;
import java.util.List;

import com.cs525.reversi.repositories.MoveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.cs525.reversi.req.CellLocation;
import com.cs525.reversi.util.moderator.GameModerator;

@Component
public class MinMaxAlgorithm implements Algorithm {
	@Autowired
	private GameModerator gameModerator;

	@Autowired
	private MoveRepository moveRepository;
	
	@Value("${reversi.default-player.is-black}")
	private boolean isBlack;
	private static  CellValue homePlayer;
	private int depth = 4;
	private static int nodesVisited = 0;

	@Override
	public MoveScore decideMove(List<MoveScore> movePoints, Game game) {
		if(movePoints == null || movePoints.isEmpty()) return null;
		List<MatrixRow> gameBoard = game.getRows();
		Move firstMove = moveRepository.findTopByGameOrderByIdDesc(game);
		boolean isStarter = firstMove == null || gameModerator.getPlayerCellValue(game, firstMove.getPlayer()) == movePoints.get(0).getNewCellValue();
		System.out.println("Lets Decide A Killing Move !!");
		int bestScore = Integer.MIN_VALUE;
		homePlayer = isBlack ? CellValue.BLACK : CellValue.WHITE;
		MoveScore bestMove = null;
		for (MoveScore move : movePoints) {

			List<MatrixRow> newBoard = getNewBoard(gameBoard, move, homePlayer);

			int childScore = runMinMax(newBoard, homePlayer, depth - 1, false);
			if(childScore > bestScore) {
				bestScore = childScore;
				bestMove = move;
			}

		}
		System.out.println("Nodes Visited = " + nodesVisited);
		System.out.println("Killing Move is : " + bestMove);
		return bestMove;
	}

	private int runMinMax(List<MatrixRow> board, CellValue player, int depth, boolean max) {
		nodesVisited++;
		if (depth == 0) {
			int ev = evaluate(board, player);
			return ev;
		}
		CellValue opPlayer = (player == CellValue.BLACK) ? CellValue.WHITE : CellValue.BLACK;

		if((max && !hasRemainingMoves(board, player)) || (!max && !hasRemainingMoves(board, opPlayer))) {
			return runMinMax(board, player, depth-1, !max);
		}
		int score = 0;
		if(max) {
			score = Integer.MIN_VALUE;
			for (MoveScore move : gameModerator.nextPossibleMoves(board, player)) {
				List<MatrixRow> newBoard = getNewBoard(board, move, player);

				int childScore = runMinMax(newBoard, player, depth - 1, false);
				if(childScore > score) score = childScore;
			}
		}else {
			score = Integer.MAX_VALUE;
			for (MoveScore move : gameModerator.nextPossibleMoves(board, opPlayer)) {

				List<MatrixRow> newBoard = getNewBoard(board, move, opPlayer);

				int childScore = runMinMax(newBoard, player, depth - 1, true);
				if(childScore < score) score = childScore;
			}
		}
		return score;
	}

	private boolean hasRemainingMoves(List<MatrixRow> board, CellValue player) {
		return !gameModerator.nextPossibleMoves(board, player).isEmpty();
	}



	private int evaluate(List<MatrixRow> board, CellValue homePlayer) {
		int mobilityScore = evaluateMobility(board, homePlayer);
		int cornerScore = evaluateCorners(board, homePlayer);
		int discDiffSc = evaluateDiscDiff(board, homePlayer);
		return mobilityScore + cornerScore + discDiffSc;

	}

	private int evaluateDiscDiff(List<MatrixRow> board, CellValue homePlayer) {
		CellValue opPlayer = (homePlayer == CellValue.BLACK) ? CellValue.WHITE : CellValue.BLACK;
		int myCount = getStoneCount(board, homePlayer);
		int opCount = getStoneCount(board, opPlayer);
		return 100 * (myCount - opCount) / (myCount + opCount);
	}

	private int getStoneCount(List<MatrixRow> board, CellValue player) {
		int score = 0;
		for(MatrixRow row : board) {
			for(CellValueClass cell : row.getCells()) {
				if(cell.getCellValue() == player) score++;
			}
		}
		return score;
	}

	private int evaluateMobility(List<MatrixRow> board, CellValue homePlayer) {
		CellValue opPlayer = (homePlayer == CellValue.BLACK) ? CellValue.WHITE : CellValue.BLACK;
		int homeMoveCount = gameModerator.nextPossibleMoves(board, homePlayer).size();
		int opMoveCount = gameModerator.nextPossibleMoves(board, opPlayer).size();
		return 100 * (homeMoveCount - opMoveCount) / (homeMoveCount + opMoveCount + 1);
	}

	private int evaluateCorners(List<MatrixRow> board, CellValue homePlayer) {
		CellValue opPlayer = (homePlayer == CellValue.BLACK) ? CellValue.WHITE : CellValue.BLACK;

		int myCorners = 0;
		int opCorners = 0;
		
		if(board.get(0).getCells().get(0).getCellValue() == homePlayer) myCorners++;
		if(board.get(7).getCells().get(0).getCellValue() == homePlayer) myCorners++;
		if(board.get(0).getCells().get(7).getCellValue() == homePlayer) myCorners++;
		if(board.get(7).getCells().get(7).getCellValue() == homePlayer) myCorners++;

		if(board.get(0).getCells().get(0).getCellValue() == opPlayer) opCorners++;
		if(board.get(7).getCells().get(0).getCellValue() == opPlayer) opCorners++;
		if(board.get(0).getCells().get(7).getCellValue() == opPlayer) opCorners++;
		if(board.get(7).getCells().get(7).getCellValue() == opPlayer) opCorners++;

		return 100 * (myCorners - opCorners) / (myCorners + opCorners + 1);
	}

	private List<MatrixRow> getNewBoard(List<MatrixRow> board, MoveScore move, CellValue player) {
		List<MatrixRow> tempBoard = new ArrayList<>();
		for (int row = 0; row < board.size(); row++) {
			List<CellValueClass> cellValues = new ArrayList<>();
			for (int cell = 0; cell < board.get(row).getCells().size(); cell++) {
				CellValue cellValue = board.get(row).getCells().get(cell).getCellValue();
				CellValueClass cellValueClass = new CellValueClass();
				cellValueClass.setCellValue(cellValue);
				cellValues.add(cellValueClass);
			}
			MatrixRow newRow = new MatrixRow();
			newRow.setId(row+1);
			newRow.setCells(cellValues);
			tempBoard.add(newRow);
		}

		tempBoard.get(move.getCellLocation().getRow()).getCells().get(move.getCellLocation().getCol())
				.setCellValue(player);
		List<CellLocation> flips = move.getCellsToFlip();
		for (CellLocation cellLocation : flips) {
			tempBoard.get(cellLocation.getRow()).getCells().get(cellLocation.getCol()).setCellValue(player);
		}
		return tempBoard;

	}

}
