package com.cs525.reversi.util.rules;

import com.cs525.reversi.req.CellLocation;
import com.cs525.reversi.models.CellValue;
import com.cs525.reversi.models.Game;

public abstract class Rule {
    private Rule next;

    public final boolean isValid(Game game, CellLocation cellLocation, CellValue newCellValue) {
        return applyRule(game, cellLocation, newCellValue) &&
                (next == null || next.isValid(game, cellLocation, newCellValue));
    }

    protected abstract boolean applyRule(Game game, CellLocation cellLocation, CellValue newCellValue);

    public final void setNext(Rule next){
        this.next = next;
    }

}
