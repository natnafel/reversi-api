package com.cs525.reversi.util.rules;

import com.cs525.reversi.models.CellValue;
import com.cs525.reversi.models.Game;
import com.cs525.reversi.services.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class ResultsInPointsRule extends Rule {
    @Autowired
    @Lazy
    private GameService gameService;

    @Override
    public boolean applyRule(Game game, int row, int col, CellValue newCellValue) {
        return gameService.nextPossibleMoves(game.getRows(), newCellValue)
                .stream()
                .anyMatch(movePoint -> movePoint.getCol() == col && movePoint.getRow() == row);
    }
}
