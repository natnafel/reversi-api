package com.cs525.reversi.services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs525.reversi.config.Mapper;
import com.cs525.reversi.models.Game;
import com.cs525.reversi.models.GameStatus;
import com.cs525.reversi.models.MatrixRow;
import com.cs525.reversi.models.Move;
import com.cs525.reversi.models.User;
import com.cs525.reversi.repositories.GameRepository;
import com.cs525.reversi.repositories.MoveRepository;
import com.cs525.reversi.repositories.UserRepository;
import com.cs525.reversi.req.NewGame;
import com.cs525.reversi.resp.Dto;
import com.cs525.reversi.resp.MoveResponse;

@Service
public class GameServiceImpl implements GameService {

	@Autowired
	private GameRepository gameRepo;
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private MoveRepository moveRepo;
	@Autowired
	private Mapper mapper;

	@Override
	public String createNewGame(NewGame newGameForm) {

		User player2 = userRepo.findByUsername(newGameForm.getUserName());
		User player1 = userRepo.findByUsername("comp");

		if (player2 == null) {
			player2 = new User();
			player2.setUsername(newGameForm.getUserName());
			player2.setUuid(UUID.randomUUID());
			userRepo.save(player2);
		}

		Game game = new Game();
		game.setPlayer1(player1);
		game.setPlayer2(player2);
		UUID gameUuid = UUID.randomUUID();
		game.setUuid(gameUuid);
		game.setRows(generateBoard());
		game.setStatus(GameStatus.OPEN);
		gameRepo.save(game);
		return gameUuid.toString();
	}

	private List<MatrixRow> generateBoard() {
		List<MatrixRow> rows = new ArrayList<MatrixRow>();

		for (int i = 1; i <= 8; i++) {
			MatrixRow row = new MatrixRow();
			List<String> cells = new ArrayList<String>();
			for (int j = 1; j <= 8; j++) {
				if (i == 4 && j == 4 || i == 5 && j == 5)
					cells.add("w");
				else if (i == 4 && j == 5 || i == 5 && j == 4)
					cells.add("b");
				else
					cells.add("");
			}
			row.setCells(cells);
			rows.add(row);
		}
		return rows;
	}

	@Override
	public List<Dto> getMoves(UUID gameuuid) {
		Game game = gameRepo.findByUuid(gameuuid);
		if(game == null) return new ArrayList<>();
		List<Move> moves = moveRepo.findByGameId(game.getId());
		return moves.stream().map(mapper::moveModelToResponse).collect(Collectors.toList());
	}

}
