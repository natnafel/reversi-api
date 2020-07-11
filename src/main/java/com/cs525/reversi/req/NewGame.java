package com.cs525.reversi.req;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NewGame {

	 private String userName;
	 private GameSideDesicion firstMove;
	 
}
