package com.cs525.reversi.models;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GredyAlgorithm implements Algorithm {

	@Override
	public MoveScore decideMove(List<MoveScore> movePoints, Game game) {
		if (movePoints == null || movePoints.isEmpty()) return null;
		return Collections.max(movePoints, Comparator.comparing(mp -> mp.getCellsToFlip().size()));
	}

}
