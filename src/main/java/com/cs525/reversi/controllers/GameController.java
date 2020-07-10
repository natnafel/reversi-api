package com.cs525.reversi.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cs525.reversi.req.NewGame;
import com.cs525.reversi.resp.GameResponse;
import com.cs525.reversi.resp.MoveResponse;
import com.cs525.reversi.resp.NewGameResp;
import com.cs525.reversi.services.GameService;

@RestController
@RequestMapping("/api")
public class GameController {

	@Autowired
	private GameService gameService;
	

	@PostMapping("/games")
	public ResponseEntity<?> createNewGame(@RequestBody NewGame newGameForm) {

		String gameUUid = gameService.createNewGame(newGameForm);
		return ResponseEntity.ok(new NewGameResp(gameUUid));
	}
	
	
	@GetMapping("/games/{uuid}/moves")
	public ResponseEntity<?> getMoves(@PathVariable UUID uuid){
		List<MoveResponse> moves = gameService.getMoves(uuid);
		return moves.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(moves);
	}
	
	@GetMapping("/games/{uuid}")
	public ResponseEntity<?> getGame(@PathVariable UUID uuid){
		GameResponse game = gameService.getGame(uuid);
		return game == null ? ResponseEntity.noContent().build() : ResponseEntity.ok(game);
	}
}
