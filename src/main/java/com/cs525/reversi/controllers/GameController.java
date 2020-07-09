package com.cs525.reversi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cs525.reversi.req.NewGame;
import com.cs525.reversi.resp.NewGameResp;
import com.cs525.reversi.services.GameService;

@RestController
@RequestMapping("/api")
public class GameController {

	@Autowired
	private GameService gameService;

	@PostMapping("/games")
	public ResponseEntity<?> addCourse(@RequestBody NewGame newGameForm) {

		 String gameUUid = gameService.createNewGame(newGameForm);
		return ResponseEntity.ok(new NewGameResp(gameUUid));
	}
}
