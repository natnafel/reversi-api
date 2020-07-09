package com.cs525.reversi.services;

import java.util.List;
import java.util.UUID;

import com.cs525.reversi.req.NewGame;
import com.cs525.reversi.resp.Dto;
import com.cs525.reversi.resp.MoveResponse;

public interface GameService {

	String createNewGame(NewGame newGameForm);
	List<Dto> getMoves(UUID gameuuid);

}
