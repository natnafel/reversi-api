package com.cs525.reversi.util.rules;

import com.cs525.reversi.models.CellValue;
import com.cs525.reversi.models.Game;

public abstract class Rule {
    private Rule next;

    public boolean isValid(Game game, int row, int col, CellValue newCellValue) {
        return applyRule(game, row, col, newCellValue) &&
                (next == null || next.isValid(game, row, col, newCellValue));
    }

    protected abstract boolean applyRule(Game game, int row, int col, CellValue newCellValue);

    public void setNext(Rule next){
        this.next = next;
    }

}
