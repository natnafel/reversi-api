package com.cs525.reversi.resp;


import java.util.List;

import com.cs525.reversi.req.CellLocation;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class MoveResponse {
		private int id;
	    private UserResponse player;
	    private int row;
	    private int col;
	    private List<CellLocation> cellsToFlip;

}
