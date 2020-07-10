package com.cs525.reversi.services;

import com.cs525.reversi.models.*;
import com.cs525.reversi.repositories.GameRepository;
import com.cs525.reversi.repositories.UserRepository;
import com.cs525.reversi.req.CellLocation;
import com.cs525.reversi.req.NewGame;
import com.cs525.reversi.util.iterators.*;
import com.cs525.reversi.util.rules.*;
import javafx.util.Pair;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

	private List<CellLocation> flipLocations(CellIterator iterator, CellLocation startCell, CellValue newCellValue) {
		List<CellLocation> flipLocations = new ArrayList<>();
		boolean seenCellWithSameValue = false;

		iterator.setPosition(startCell.getRow(), startCell.getCol());

		while(iterator.hasNext()){
			Pair<CellLocation, CellValue> next = iterator.next();
			if (next.getValue() == CellValue.EMPTY) break;
			if (next.getValue() == newCellValue) {
				seenCellWithSameValue = true;
				break;
			}
			flipLocations.add(next.getKey());
		}

		return seenCellWithSameValue? flipLocations: new ArrayList<>();
	}

	@Override
	public List<MoveScore> nextPossibleMoves(List<MatrixRow> rows, CellValue newCellValue) {
		List<MoveScore> result = new ArrayList<>();

		List<CellIterator> iterators = cellIterators(rows);
		int rowSize = rows.size();
		int colSize = rows.get(0).getCells().size();

		for(int row = 0; row < rowSize; row++){
			for (int col = 0; col < colSize; col++){
				List<CellLocation> cellsToFlip = new ArrayList<>();
				CellLocation newCellLocation = new CellLocation(row, col);
				for (CellIterator iterator: iterators){
					cellsToFlip.addAll(flipLocations(iterator, newCellLocation, newCellValue));
				}
				if (cellsToFlip.size() > 0){
					result.add(new MoveScore(newCellLocation, cellsToFlip));
				}
			}
		}

		return result;
	}

	@Override
	public boolean validateMove(Game game, CellLocation cellLocation, CellValue newCellValue) {
		return gameRule.isValid(game, cellLocation, newCellValue);
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
