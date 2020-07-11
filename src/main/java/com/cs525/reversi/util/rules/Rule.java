package com.cs525.reversi.util.rules;

import com.cs525.reversi.req.CellLocation;
import com.cs525.reversi.models.CellValue;
import com.cs525.reversi.models.Game;

import java.util.logging.Logger;

public abstract class Rule {
    private Rule next;

    private Logger logger = Logger.getLogger(this.getClass().getSimpleName());

    public final boolean isValid(Game game, CellLocation cellLocation, CellValue newCellValue) {
        boolean myVerdict = applyRule(game, cellLocation, newCellValue);
        if (!myVerdict) {
            logger.warning("Rule validation field");
        }
        return myVerdict &&
                (next == null || next.isValid(game, cellLocation, newCellValue));
    }

    protected abstract boolean applyRule(Game game, CellLocation cellLocation, CellValue newCellValue);

    public final void setNext(Rule next){
        this.next = next;
    }

}
