package com.cs525.reversi.util.moderator;

import com.cs525.reversi.models.*;
import com.cs525.reversi.req.CellLocation;

import java.util.List;

import org.springframework.stereotype.Component;

public interface GameModerator {
    int playerScore(Game game, User player);
    void applyMove(Game game, MoveScore moveScore);
    boolean validateMove(Game game, CellLocation cellLocation, User player);
    MoveScore moveByAlgorithmForUser(Game game, User serverUser, Algorithm algorithm);
    List<MoveScore> nextPossibleMoves(List<MatrixRow> rows, CellValue newCellValue);
    MoveScore moveScoreForNewPiece(Game game, CellLocation newCellLocation, User player);
    CellValue getPlayerCellValue(Game game, User player);
}
