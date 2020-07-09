package com.cs525.reversi.util.rules;

import com.cs525.reversi.models.CellValue;
import com.cs525.reversi.models.Game;
import org.springframework.stereotype.Component;

@Component
public class EmptyRule extends Rule {
    @Override
    public boolean applyRule(Game game, int row, int col, CellValue newCellValue) {
        return game.getRows().get(row).getCells().get(col) == CellValue.EMPTY;
    }
}
