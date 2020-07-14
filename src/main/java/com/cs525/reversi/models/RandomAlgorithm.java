package com.cs525.reversi.models;

import java.util.List;
import java.util.Random;

public class RandomAlgorithm implements Algorithm{

	@Override
	public MoveScore decideMove(List<MoveScore> movePoints, Game game) {
		if (movePoints == null || movePoints.isEmpty()) return null;
		return movePoints.get(new Random().nextInt(movePoints.size()));
	}

}
