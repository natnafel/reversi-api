package com.cs525.reversi.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs525.reversi.models.Game;
import com.cs525.reversi.models.GameBuilder;
import com.cs525.reversi.models.GameStatus;
import com.cs525.reversi.models.ReversiGameBuilder;
import com.cs525.reversi.models.User;
import com.cs525.reversi.repositories.GameRepository;
import com.cs525.reversi.repositories.UserRepository;
import com.cs525.reversi.req.NewGame;

@Service
public class GameServiceImpl implements GameService {

	@Autowired
	private GameRepository gameRepo;
	@Autowired
	private UserRepository userRepo;

	@Override
	public String createNewGame(NewGame newGameForm) {

		User player2 = userRepo.findByUsername(newGameForm.getUserName());
		User player1 = userRepo.findByUsername("comp");

		if (player2 == null) {
			player2 = new User(UUID.randomUUID(), newGameForm.getUserName());
			userRepo.save(player2);
		}

		GameBuilder reversiGameBuilder = new ReversiGameBuilder();

		Game game = reversiGameBuilder.buildPlayerOne(player1).buildPlayerTwo(player2).buildGameStatus(GameStatus.OPEN)
				.buildGameUUID().buildBoardGame().getGame();

		game.setDefaultCells();

		gameRepo.save(game);
		return game.getUuid().toString();
	}

}
