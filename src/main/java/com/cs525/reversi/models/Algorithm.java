package com.cs525.reversi.models;

import java.util.List;

public interface Algorithm {

	 Move decideMove(List<MoveScore> movePoints , List<MatrixRow> gameBoard);
}
