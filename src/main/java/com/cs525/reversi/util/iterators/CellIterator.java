package com.cs525.reversi.util.iterators;

import com.cs525.reversi.models.CellValue;
import com.cs525.reversi.models.MatrixRow;
import com.cs525.reversi.req.CellLocation;
import com.cs525.reversi.util.Pair;

import java.util.List;

public abstract class CellIterator {
    protected final List<MatrixRow> rows;
    protected int currRow = 0;
    protected int currCol = 0;
    public CellIterator (List<MatrixRow> rows) {
        this.rows = rows;
    }

    public abstract Pair<CellLocation, CellValue> next();
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
        return rows.get(row).getCells().get(col).getCellValue();
    }

    public boolean isCurrentOccupied(){
    	if(currRow==-1 && currCol==-1) {
    		return true;
    	}
        return rows.get(currRow).getCells().get(currCol).getCellValue() != CellValue.EMPTY;
    }

    protected CellLocation getCellLocation(int row, int col) {
        return new CellLocation(row, col);
    }

}
