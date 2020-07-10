package com.cs525.reversi.util.rules;

import com.cs525.reversi.req.CellLocation;
import com.cs525.reversi.models.CellValue;
import com.cs525.reversi.models.Game;
import org.springframework.stereotype.Component;

@Component
public class NewValueNotEmptyRule extends Rule {
    @Override
    protected boolean applyRule(Game game, CellLocation cellLocation, CellValue newCellValue) {
        return newCellValue != CellValue.EMPTY;
    }
}
