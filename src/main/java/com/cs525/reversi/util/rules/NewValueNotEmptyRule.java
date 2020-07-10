package com.cs525.reversi.util.rules;

import com.cs525.reversi.models.CellValue;
import com.cs525.reversi.models.Game;
import org.springframework.stereotype.Component;

@Component
public class NewValueNotEmptyRule extends Rule {
    @Override
    protected boolean applyRule(Game game, int row, int col, CellValue newCellValue) {
        return newCellValue != CellValue.EMPTY;
    }
}
