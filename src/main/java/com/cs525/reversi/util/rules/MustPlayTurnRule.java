package com.cs525.reversi.util.rules;

import com.cs525.reversi.models.CellValue;
import com.cs525.reversi.models.Game;
import com.cs525.reversi.req.CellLocation;
import com.cs525.reversi.util.moderator.GameModerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class MustPlayTurnRule extends Rule {
    @Autowired
    @Lazy
    private GameModerator gameModerator;

    @Override
    protected boolean applyRule(Game game, CellLocation cellLocation, CellValue newCellValue) {
        if (cellLocation != null && cellLocation.getRow() >= 0 && cellLocation.getCol() >= 0) return true;
        return gameModerator.nextPossibleMoves(game.getRows(), newCellValue).isEmpty();
    }
}
