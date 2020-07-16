package com.cs525.reversi.models;

import com.cs525.reversi.req.CellLocation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GredyAlgorithm implements Algorithm {

	@Override
	public MoveScore decideMove(List<MoveScore> movePoints, Game game) {
		if (movePoints == null || movePoints.isEmpty()) return new MoveScore(CellValue.EMPTY, new CellLocation(-1, -1), new ArrayList<>());
		return Collections.max(movePoints, Comparator.comparing(mp -> mp.getCellsToFlip().size()));
	}

}
