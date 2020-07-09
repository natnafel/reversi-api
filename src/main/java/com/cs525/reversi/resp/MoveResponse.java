package com.cs525.reversi.resp;


import com.cs525.reversi.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class MoveResponse implements Dto{
		private int id;
	    //private User player;
	    private int roww;
	    private int col;
}
