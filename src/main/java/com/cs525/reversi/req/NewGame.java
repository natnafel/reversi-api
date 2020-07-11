package com.cs525.reversi.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewGame {

	 private String userName;
	 private GameSideDesicion firstMove;
	 
}
