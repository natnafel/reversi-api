package com.cs525.reversi.resp;


import com.cs525.reversi.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class MoveResponse {
		private int id;
	    private UserResponse player;
	    private int row;
	    private int col;
}
