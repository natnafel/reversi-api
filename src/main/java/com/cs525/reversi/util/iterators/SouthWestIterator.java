package com.cs525.reversi.util.iterators;

import com.cs525.reversi.models.CellValue;
import com.cs525.reversi.models.MatrixRow;

import java.util.List;

public class SouthWestIterator extends CellIterator {
    public SouthWestIterator(List<MatrixRow> rows) {
        super(rows);
    }

    @Override
    public CellValue next() {
        return getCellValue(++currRow, --currCol);
    }

    @Override
    public boolean hasNext() {
        return !rowAtEnd() && !colAtStart();
    }
}