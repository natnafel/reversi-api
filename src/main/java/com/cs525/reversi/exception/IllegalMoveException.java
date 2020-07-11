package com.cs525.reversi.exception;

import com.cs525.reversi.req.CellLocation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class IllegalMoveException extends ReversiException {
    public IllegalMoveException(CellLocation cellLocation) {
        super(String.format("Move at row %s and col %s is not allowed on current board", cellLocation.getRow(), cellLocation.getCol()));
    }
}
