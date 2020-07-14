package com.cs525.reversi.models;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.cs525.reversi.util.moderator.GameModerator;



public class MinMaxAlgorithm implements Algorithm {
	@Autowired
	private GameModerator gameModerator;
	
	private static final CellValue homePlayer = CellValue.BLACK;
	private int depth = 6;
	private static int nodesVisited = 0;

	@Override
	public MoveScore decideMove(List<MoveScore> movePoints, List<MatrixRow> gameBoard) {
		int bestScore = Integer.MIN_VALUE;
		MoveScore bestMove = null;
		for (MoveScore move : movePoints) {
			List<MatrixRow> newBoard = getNewBoard(gameBoard, move, homePlayer);
			int childScore = runMinMax(newBoard, homePlayer, depth - 1, false);
			if(childScore > bestScore) {
				bestScore = childScore;
				bestMove = move;
			}

		}
		System.out.println("Nodes visited = " + nodesVisited);
		return bestMove;
	}

	private int runMinMax(List<MatrixRow> board, CellValue homePlayer, int depth, boolean max) {
		nodesVisited++;
		if (depth == 0) {
			return evaluate(board, homePlayer);
		}
		CellValue opPlayer = (homePlayer == CellValue.BLACK) ? CellValue.WHITE : CellValue.BLACK;
		if((max && !hasRemainingMoves(board, homePlayer)) || (!max && !hasRemainingMoves(board, opPlayer))) {
			return runMinMax(board, homePlayer, depth-1, !max);
		}
		int score = 0;
		if(max) {
			score = Integer.MIN_VALUE;
			for (MoveScore move : gameModerator.nextPossibleMoves(board, homePlayer)) {
				List<MatrixRow> newBoard = getNewBoard(board, move, homePlayer);
				int childScore = runMinMax(newBoard, homePlayer, depth - 1, false);
				if(childScore > score) score = childScore;
			}
		}else {
			score = Integer.MAX_VALUE;
			for (MoveScore move : gameModerator.nextPossibleMoves(board, opPlayer)) {
				List<MatrixRow> newBoard = getNewBoard(board, move, opPlayer);
				int childScore = runMinMax(newBoard, homePlayer, depth - 1, true);
				if(childScore < score) score = childScore;
			}
		}
		return score;
	}

	private boolean hasRemainingMoves(List<MatrixRow> board, CellValue player) {
		return gameModerator.nextPossibleMoves(board, player).size() > 0;
	}

	private int evaluate(List<MatrixRow> board, CellValue homePlayer) {
		int mobilityScore = evaluateMobility(board, homePlayer);
		int cornerScore = evaluateCorners(board, homePlayer);
		int discDiffSc = evaluateDiscDiff(board, homePlayer);
		return 2*mobilityScore + discDiffSc + 1000*cornerScore;

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

	private List<MatrixRow> getNewBoard(List<MatrixRow> board, MoveScore move, CellValue Player) {
		List<MatrixRow> tempBoard = new ArrayList<>();
		for (int row = 0; row < board.size(); row++) {
			for (int cell = 0; cell < board.get(row).getCells().size(); cell++) {
				CellValue cellValue = board.get(row).getCells().get(cell).getCellValue();
				tempBoard.get(row).getCells().get(cell).setCellValue(cellValue);
			}
		}
		tempBoard.get(move.getCellLocation().getRow()).getCells().get(move.getCellLocation().getCol())
				.setCellValue(homePlayer);

		return tempBoard;

	}

}
