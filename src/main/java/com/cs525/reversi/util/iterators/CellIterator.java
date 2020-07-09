package com.cs525.reversi.util.iterators;

import com.cs525.reversi.models.CellValue;
import com.cs525.reversi.models.MatrixRow;

import java.util.List;

public abstract class CellIterator {
    protected final List<MatrixRow> rows;
    protected int currRow = 0;
    protected int currCol = 0;
    public CellIterator (List<MatrixRow> rows) {
        this.rows = rows;
    }

    public abstract CellValue next();
    public abstract boolean hasNext();

    public void setPosition(int row, int col) {
        this.currRow = row;
        this.currCol = col;
    }

    protected boolean rowAtEnd(){
        return currRow == rows.size() - 1;
    }

    protected boolean rowAtStart(){
        return currRow == 0;
    }

    protected boolean colAtEnd(){
        return currCol == rows.get(currCol).getCells().size() - 1;
    }

    protected boolean colAtStart() {
        return currCol == 0;
    }

    protected CellValue getCellValue(int row, int col){
        return rows.get(row).getCells().get(col);
    }
}
