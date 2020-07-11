package com.cs525.reversi.models;

import java.util.List;
import java.util.Random;

public class RandomAlgorithm implements Algorithm{

	@Override
	public MoveScore decideMove(List<MoveScore> movePoints, List<MatrixRow> gameBoard) {
		// TODO Auto-generated method stub
		return movePoints.get(new Random().nextInt(movePoints.size()-1));
	}

}
