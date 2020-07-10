package com.cs525.reversi.util.rules;

import com.cs525.reversi.req.CellLocation;
import com.cs525.reversi.models.CellValue;
import com.cs525.reversi.models.Game;
import com.cs525.reversi.models.GameStatus;
import org.springframework.stereotype.Component;

@Component
public class OpenGameRule extends Rule {
    @Override
    public boolean applyRule(Game game, CellLocation cellLocation, CellValue newCellValue) {
        return game.getStatus() == GameStatus.OPEN;
    }
}
