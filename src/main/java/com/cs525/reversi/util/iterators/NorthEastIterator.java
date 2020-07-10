package com.cs525.reversi.util.iterators;

import com.cs525.reversi.models.CellValue;
import com.cs525.reversi.models.MatrixRow;
import com.cs525.reversi.req.CellLocation;
import javafx.util.Pair;

import java.util.List;

public class NorthEastIterator extends CellIterator {
    public NorthEastIterator(List<MatrixRow> rows) {
        super(rows);
    }

    @Override
    public Pair<CellLocation, CellValue> next() {
        return new Pair<>(getCellLocation(--currRow, ++currCol), getCellValue(currRow, currCol));
    }

    @Override
    public boolean hasNext() {
        return !rowAtStart() && !colAtEnd();
    }
}
