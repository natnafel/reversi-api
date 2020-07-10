package com.cs525.reversi.services;

import java.util.UUID;
import java.util.stream.Collectors;

import com.cs525.reversi.models.*;
import com.cs525.reversi.util.iterators.*;
import com.cs525.reversi.util.rules.*;
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

	private final GameRepository gameRepo;
	private final UserRepository userRepo;
	private final Rule gameRule;

	public GameServiceImpl(GameRepository gameRepo, UserRepository userRepo,
						   EmptyRule emptyRule, OpenGameRule openGameRule, NewValueNotEmptyRule newValueNotEmptyRule,
						   ResultsInPointsRule resultsInPointsRule) {
		this.gameRepo = gameRepo;
		this.userRepo = userRepo;
		this.gameRule = emptyRule;
		emptyRule.setNext(openGameRule);
		openGameRule.setNext(newValueNotEmptyRule);
		newValueNotEmptyRule.setNext(resultsInPointsRule);
	}


	public List<CellIterator> cellIterators(List<MatrixRow> rows){
		List<CellIterator> iterators = new ArrayList<>();
		iterators.add(new NorthIterator(rows));
		iterators.add(new SouthIterator(rows));
		iterators.add(new WestIterator(rows));
		iterators.add(new EastIterator(rows));
		iterators.add(new NorthEastIterator(rows));
		iterators.add(new SouthEastIterator(rows));
		iterators.add(new SouthWestIterator(rows));
		iterators.add(new NorthWestIterator(rows));
		return iterators;
	}

	private int points(CellIterator iterator, int startRow, int startCol, CellValue newCellValue) {
		int points = 0;
		boolean seenCellWithSameValue = false;

		iterator.setPosition(startRow, startCol);

		while(iterator.hasNext()){
			CellValue next = iterator.next();
			if (next == CellValue.EMPTY) break;
			if (next == newCellValue) {
				seenCellWithSameValue = true;
				break;
			}
			points++;
		}

		return seenCellWithSameValue? points: 0;
	}

	@Override
	public List<MovePoint> nextPossibleMoves(List<MatrixRow> rows, CellValue newCellValue) {
		List<MovePoint> result = new ArrayList<>();

		List<CellIterator> iterators = cellIterators(rows);
		int rowSize = rows.size();
		int colSize = rows.get(0).getCells().size();

		for(int row = 0; row < rowSize; row++){
			for (int col = 0; col < colSize; col++){
				int points = 0;
				for (CellIterator iterator: iterators){
					points += points(iterator, row, col, newCellValue);
				}
				if (points > 0){
					result.add(new MovePoint(row, col, points));
				}
			}
		}

		return result;
	}

	@Override
	public boolean validateMove(Game game, int row, int col, CellValue newCellValue) {
		return gameRule.isValid(game, row, col, newCellValue);
	}

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
