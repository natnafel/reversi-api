package com.cs525.reversi.controllers;

import java.util.List;
import java.util.UUID;

import com.cs525.reversi.req.AwayGameRequest;
import com.cs525.reversi.req.CellLocation;
import com.cs525.reversi.req.NewGame;
import com.cs525.reversi.resp.AwayGameResponse;
import com.cs525.reversi.resp.NewGameAndMoveResp;
import com.cs525.reversi.services.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cs525.reversi.resp.GameResponse;
import com.cs525.reversi.resp.MoveResponse;


@RestController
@RequestMapping("/api")
public class GameController {

	@Autowired
	private GameService gameService;
	

	@PostMapping("/games")
	public NewGameAndMoveResp createNewGame(@RequestBody NewGame newGameForm) {
		return gameService.createNewGame(newGameForm);
	}

	@PostMapping(value = "/games", params = "type=away")
	public AwayGameResponse startAwayGame(@RequestBody AwayGameRequest awayGameRequest) {
		return gameService.startAwayGame(awayGameRequest);
	}

	@PostMapping("/games/{gameUUID}/move")
	public NewGameAndMoveResp makeMove(@PathVariable UUID gameUUID, @RequestBody CellLocation newMoveLocation) {
		return gameService.makeMoveForOpponent(gameUUID, newMoveLocation);
	}
	
	@GetMapping("/algorithms")
	public ResponseEntity<?> getSupportedAlgorithm() {
		return ResponseEntity.ok(gameService.getSupportedAlgorithms());
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
