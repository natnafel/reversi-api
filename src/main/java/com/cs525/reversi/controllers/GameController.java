package com.cs525.reversi.controllers;

import com.cs525.reversi.req.NewGame;
import com.cs525.reversi.services.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class GameController {

	@Autowired
	private GameService gameService;

	@PostMapping("/games")
	public ResponseEntity<?> createNewGame(@RequestBody NewGame newGameForm) {
		return ResponseEntity.ok(gameService.createNewGame(newGameForm));
	}
}
