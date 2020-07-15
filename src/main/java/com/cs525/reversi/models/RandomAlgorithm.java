package com.cs525.reversi.models;

import com.cs525.reversi.req.CellLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomAlgorithm implements Algorithm{

	@Override
	public MoveScore decideMove(List<MoveScore> movePoints, Game game) {
		if (movePoints == null || movePoints.isEmpty()) return new MoveScore(CellValue.EMPTY, new CellLocation(-1, -1), new ArrayList<>());
		return movePoints.get(new Random().nextInt(movePoints.size()));
	}

}
