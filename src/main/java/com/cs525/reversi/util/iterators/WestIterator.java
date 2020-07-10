package com.cs525.reversi.util.iterators;

import com.cs525.reversi.models.CellValue;
import com.cs525.reversi.models.MatrixRow;
import com.cs525.reversi.req.CellLocation;
import com.cs525.reversi.util.Pair;

import java.util.List;

public class WestIterator extends CellIterator {

    public WestIterator(List<MatrixRow> rows) {
        super(rows);
    }

    @Override
    public Pair<CellLocation, CellValue> next() {
        return new Pair<>(getCellLocation(currRow, --currCol), getCellValue(currRow, currCol));
    }

    @Override
    public boolean hasNext() {
        return !colAtStart();
    }
}
