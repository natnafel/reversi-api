package com.cs525.reversi.util.iterators;

import com.cs525.reversi.models.CellValue;
import com.cs525.reversi.models.MatrixRow;

import java.util.List;

public class NorthWestIterator extends CellIterator{
    public NorthWestIterator(List<MatrixRow> rows) {
        super(rows);
    }

    @Override
    public CellValue next() {
        return getCellValue(--currRow, --currCol);
    }

    @Override
    public boolean hasNext() {
        return !rowAtStart() && !colAtStart();
    }
}
