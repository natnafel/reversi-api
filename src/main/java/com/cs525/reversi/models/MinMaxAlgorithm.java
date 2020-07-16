package com.cs525.reversi.models;

import java.util.ArrayList;
import java.util.List;

import com.cs525.reversi.repositories.MoveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
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
	public int depth;
	private static int nodesVisited = 0;
	private static boolean isStarter;
	public boolean isPruning;
	public AlgorithmMode weights;
	
	 
	
	

	@Override
	public MoveScore decideMove(List<MoveScore> movePoints, Game game) {
		System.out.println(" Depth = " + depth);
		System.out.println("Pruning = " + isPruning);
		System.out.println("Weights = " + weights);
		if (movePoints == null || movePoints.isEmpty()) return new MoveScore(CellValue.EMPTY, new CellLocation(-1, -1), new ArrayList<>());
		List<MatrixRow> gameBoard = game.getRows();
		Move firstMove = moveRepository.findTopByGameOrderByIdDesc(game);
		isStarter = firstMove == null || gameModerator.getPlayerCellValue(game, firstMove.getPlayer()) == movePoints.get(0).getNewCellValue();
		System.out.println("Lets Decide A Killing Move !!");
		int bestScore = Integer.MIN_VALUE;
		homePlayer = isBlack ? CellValue.BLACK : CellValue.WHITE;
		MoveScore bestMove = null;
		if(isPruning) {
			for (MoveScore move : movePoints) {

				List<MatrixRow> newBoard = getNewBoard(gameBoard, move, homePlayer);

				int childScore = runMinMaxPruning(newBoard, homePlayer, depth - 1, false, Integer.MIN_VALUE, Integer.MAX_VALUE);
				if(childScore > bestScore) {
					bestScore = childScore;
					bestMove = move;
				}

			}
		}else {
			for (MoveScore move : movePoints) {

				List<MatrixRow> newBoard = getNewBoard(gameBoard, move, homePlayer);

				int childScore = runMinMax(newBoard, homePlayer, depth - 1, false);
				if(childScore > bestScore) {
					bestScore = childScore;
					bestMove = move;
				}

			}
		}
		
		System.out.println("Nodes Visited = " + nodesVisited);
		System.out.println("Killing Move is : " + bestMove);
		nodesVisited = 0;
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
			List<MoveScore> moves = gameModerator.nextPossibleMoves(board, opPlayer);
			for (MoveScore move : moves) {

				List<MatrixRow> newBoard = getNewBoard(board, move, opPlayer);

				int childScore = runMinMax(newBoard, player, depth - 1, true);
				if(childScore < score) score = childScore;
			}
		}
		return score;
	}

	private int runMinMaxPruning(List<MatrixRow> board, CellValue player, int depth, boolean max, int alpha, int beta) {
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

				int childScore = runMinMaxPruning(newBoard, player, depth - 1, false, alpha, beta);
				if(childScore > score) score = childScore;
				if(score > alpha) alpha = score;
	            if(beta <= alpha) break;
			}
		}else {
			score = Integer.MAX_VALUE;
			List<MoveScore> moves = gameModerator.nextPossibleMoves(board, opPlayer);
			for (MoveScore move : moves) {

				List<MatrixRow> newBoard = getNewBoard(board, move, opPlayer);

				int childScore = runMinMaxPruning(newBoard, player, depth - 1, true, alpha, beta);
				if(childScore < score) score = childScore; 
				if(score < beta) beta = score;
                if(beta <= alpha) break;
			}
		}
		return score;
	}

	private boolean hasRemainingMoves(List<MatrixRow> board, CellValue player) {
		return !gameModerator.nextPossibleMoves(board, player).isEmpty();
	}

	private int evaluate(List<MatrixRow> board, CellValue homePlayer) {
		int mobilityScore = 0;
		int cornerScore = 0;
		int discDiffSc = 0;
		int xSquareScore = 0;
		int cSquareScore = 0;
		
		if(weights == AlgorithmMode.MAXIMAL_WEIGHTS) {
			mobilityScore = evaluateMobility(board, homePlayer) * 100;
			cornerScore = evaluateCorners(board, homePlayer) * 200;
			discDiffSc = evaluateDiscDiff(board, homePlayer) * 30;
			xSquareScore = evaluateXSquare(board, homePlayer) * 100;
			cSquareScore = evaluateCSquare(board, homePlayer) * 50;
		}else if(weights == AlgorithmMode.MINIMAL_WEIGHTS){
			mobilityScore = evaluateMobility(board, homePlayer) * 2;
			cornerScore = evaluateCorners(board, homePlayer) * 1000;
			discDiffSc = evaluateDiscDiff(board, homePlayer);
		}else {
			cornerScore = evaluateCorners(board, homePlayer) * 100;
			discDiffSc = evaluateDiscDiff(board, homePlayer);
		}
		
		return mobilityScore + cornerScore + discDiffSc + xSquareScore + cSquareScore;
	}

	private int evaluateDiscDiff(List<MatrixRow> board, CellValue homePlayer) {
		CellValue opPlayer = (homePlayer == CellValue.BLACK) ? CellValue.WHITE : CellValue.BLACK;
		int myCount = getStoneCount(board, homePlayer);
		int opCount = getStoneCount(board, opPlayer);
		int diff = 100 * (myCount - opCount) / (myCount + opCount);
		if (myCount + opCount < 40) diff *= -1;
		return diff;
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

	private int evaluateXSquare(List<MatrixRow> board, CellValue homePlayer) {
		CellValue opPlayer = (homePlayer == CellValue.BLACK) ? CellValue.WHITE : CellValue.BLACK;

		int mySquares = xSquareHelper(board, CellValue.EMPTY);
		int opSquares = xSquareHelper(board, CellValue.EMPTY);

		return -1 * 100 * (mySquares - opSquares) / (mySquares + opSquares + 1);
	}

	private int evaluateCSquare(List<MatrixRow> board, CellValue homePlayer) {
		CellValue opPlayer = (homePlayer == CellValue.BLACK) ? CellValue.WHITE : CellValue.BLACK;

		int mySquares = cSquareHelper(board, homePlayer);
		int opSquares = cSquareHelper(board, opPlayer);

		return -1 * 100 * (mySquares - opSquares) / (mySquares + opSquares + 1);
	}

	private int xSquareHelper(List<MatrixRow> board, CellValue cellValue){
		int squares = 0;

		if(board.get(1).getCells().get(1).getCellValue() == cellValue) {
			squares = board.get(0).getCells().get(0).getCellValue() != cellValue ? squares++ : squares--;
		}
		if(board.get(1).getCells().get(6).getCellValue() == cellValue) {
			squares = board.get(0).getCells().get(7).getCellValue() != cellValue ? squares++ : squares--;
		}
		if(board.get(6).getCells().get(6).getCellValue() == cellValue) {
			squares = board.get(7).getCells().get(7).getCellValue() != cellValue ? squares++ : squares--;
		}
		if(board.get(6).getCells().get(1).getCellValue() == cellValue) {
			squares = board.get(7).getCells().get(0).getCellValue() != cellValue ? squares++ : squares--;
		}

		return squares;
	}

	private int cSquareHelper(List<MatrixRow> board, CellValue cellValue) {
		int squares = 0;

		if(board.get(0).getCells().get(1).getCellValue() == cellValue){
			squares = board.get(0).getCells().get(0).getCellValue() != cellValue ? squares++ : squares--;
		}
		if(board.get(1).getCells().get(0).getCellValue() == cellValue) {
			squares = board.get(0).getCells().get(0).getCellValue() != cellValue ? squares++ : squares--;
		}

		if(board.get(0).getCells().get(6).getCellValue() == cellValue) {
			squares = board.get(0).getCells().get(7).getCellValue() != cellValue ? squares++ : squares--;
		}
		if(board.get(1).getCells().get(7).getCellValue() == cellValue) {
			squares = board.get(0).getCells().get(7).getCellValue() != cellValue ? squares++ : squares--;
		}

		if(board.get(6).getCells().get(0).getCellValue() == cellValue) {
			squares = board.get(7).getCells().get(0).getCellValue() != cellValue ? squares++ : squares--;
		}

		if(board.get(7).getCells().get(1).getCellValue() == cellValue) {
			squares = board.get(7).getCells().get(0).getCellValue() != cellValue ? squares++ : squares--;
		}

		if(board.get(6).getCells().get(7).getCellValue() == cellValue) {
			squares = board.get(7).getCells().get(7).getCellValue() != cellValue ? squares++ :squares--;
		}
		if(board.get(7).getCells().get(6).getCellValue() == cellValue) {
			squares = board.get(7).getCells().get(7).getCellValue() != cellValue ? squares++ : squares--;
		}

		return squares;
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
