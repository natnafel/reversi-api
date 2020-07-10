package com.cs525.reversi.util.rules;

import com.cs525.reversi.req.CellLocation;
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
    public boolean applyRule(Game game, CellLocation cellLocation, CellValue newCellValue) {
        return gameService.nextPossibleMoves(game.getRows(), newCellValue)
                .stream()
                .anyMatch(movePoint -> movePoint.getCellLocation() == cellLocation && movePoint.getCellsToFlip().size() > 0);
    }
}
