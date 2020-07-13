package com.cs525.reversi.models;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.cs525.reversi.req.CellLocation;
import com.cs525.reversi.util.moderator.GameModerator;

@Component
public class MinMaxAlgorithm implements Algorithm {
	@Autowired
	private GameModerator gameModerator;
	
	@Value("${reversi.default-player.is-black}")
	private boolean isBlack;
	private static  CellValue homePlayer;
	private int depth = 3;
	private static int nodesVisited = 0;

	@Override
	public MoveScore decideMove(List<MoveScore> movePoints, List<MatrixRow> gameBoard) {
		if(movePoints.isEmpty() || movePoints == null) return null;
		System.out.println("Lets Decide A Killing Move !!");
		int bestScore = Integer.MIN_VALUE;
		homePlayer = isBlack ? CellValue.BLACK : CellValue.WHITE;
		MoveScore bestMove = null;
//		System.out.println("Number of moves to check : " + movePoints.size());
		for (MoveScore move : movePoints) {
//			System.out.println("MAIN GAME BOARD");
//			gameBoard.forEach(System.out::println);
//			System.out.println("MAIN MOVE");
//			System.out.println(move);
			List<MatrixRow> newBoard = getNewBoard(gameBoard, move, homePlayer);
//			System.out.println("simulated my move");
//			newBoard.forEach(System.out::println);
			int childScore = runMinMax(newBoard, homePlayer, depth - 1, false);
			if(childScore > bestScore) {
				bestScore = childScore;
				bestMove = move;
			}

		}
		System.out.println("Nodes Visited = " + nodesVisited);
//		System.out.println("best score is : " + bestScore);
		System.out.println("Killing Move is : " + bestMove);
//		System.out.println("Original game board");
//		gameBoard.forEach(System.out::println);
		return bestMove;
	}

	private int runMinMax(List<MatrixRow> board, CellValue player, int depth, boolean max) {
//		System.out.println("Apply minimax");
		nodesVisited++;
		if (depth == 0) {
//			System.out.println("Depth is Zero");
			int ev = evaluate(board, player);
//			System.out.println(" evaluation " + ev );
			return ev;
		}
		CellValue opPlayer = (player == CellValue.BLACK) ? CellValue.WHITE : CellValue.BLACK;

		if((max && !hasRemainingMoves(board, player)) || (!max && !hasRemainingMoves(board, opPlayer))) {
//			System.out.println("No remaining moves");
			return runMinMax(board, player, depth-1, !max);
		}
		int score = 0;
		if(max) {
			score = Integer.MIN_VALUE;
//			System.out.println("My turn");
//			System.out.println("Number of moves to check : " + gameModerator.nextPossibleMoves(board, player).size());
			for (MoveScore move : gameModerator.nextPossibleMoves(board, player)) {
//				System.out.println(move.hashCode());
//				System.out.println(move);
				List<MatrixRow> newBoard = getNewBoard(board, move, player);
//				System.out.println("Simulated my move");
//				newBoard.forEach(System.out::println);
				int childScore = runMinMax(newBoard, player, depth - 1, false);
				if(childScore > score) score = childScore;
			}
		}else {
			score = Integer.MAX_VALUE;
//			System.out.println("Opponent turn");
//			System.out.println("Number of moves to check : " + gameModerator.nextPossibleMoves(board, player).size());
			for (MoveScore move : gameModerator.nextPossibleMoves(board, opPlayer)) {
//				System.out.println(move.hashCode());
//				System.out.println(move);
				List<MatrixRow> newBoard = getNewBoard(board, move, opPlayer);
//				System.out.println("Simulated opponent move");
//				newBoard.forEach(System.out::println);
				int childScore = runMinMax(newBoard, player, depth - 1, true);
				if(childScore < score) score = childScore;
			}
		}
//		System.out.println("Score = : " + score);
		return score;
	}

	private boolean hasRemainingMoves(List<MatrixRow> board, CellValue player) {
		return !gameModerator.nextPossibleMoves(board, player).isEmpty();
	}



	private int evaluate(List<MatrixRow> board, CellValue homePlayer) {
		int mobilityScore = evaluateMobility(board, homePlayer);
		int cornerScore = evaluateCorners(board, homePlayer);
		int discDiffSc = evaluateDiscDiff(board, homePlayer);
		return 2 * mobilityScore + discDiffSc + 1000*cornerScore;

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
