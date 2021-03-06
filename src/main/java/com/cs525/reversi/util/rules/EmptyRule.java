package com.cs525.reversi.util.rules;

import com.cs525.reversi.req.CellLocation;
import com.cs525.reversi.models.CellValue;
import com.cs525.reversi.models.Game;
import org.springframework.stereotype.Component;

@Component
public class EmptyRule extends Rule {
    @Override
    public boolean applyRule(Game game, CellLocation cellLocation, CellValue newCellValue) {
        if (cellLocation.getRow() < 0 && cellLocation.getCol() < 0) return true;
        return game.getRows().get(cellLocation.getRow()).getCells().get(cellLocation.getCol()).getCellValue() == CellValue.EMPTY;
    }
}
