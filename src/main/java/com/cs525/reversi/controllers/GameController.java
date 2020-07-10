package com.cs525.reversi.controllers;

import com.cs525.reversi.models.Game;
import com.cs525.reversi.req.NewGame;
import com.cs525.reversi.services.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cs525.reversi.req.NewGame;
import com.cs525.reversi.services.GameService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class GameController {

	@Autowired
	private GameService gameService;

	@PostMapping("/games")
	public ResponseEntity<?> createNewGame(@RequestBody NewGame newGameForm) {
		return ResponseEntity.ok(gameService.createNewGame(newGameForm));
	}
	@GetMapping("/games")
	public List<Game> getListOfGames(){
		return gameService.getAll();
	}
}
