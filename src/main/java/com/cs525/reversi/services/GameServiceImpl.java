package com.cs525.reversi.services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.cs525.reversi.models.*;
import com.cs525.reversi.util.iterators.*;
import com.cs525.reversi.util.rules.EmptyRule;
import com.cs525.reversi.util.rules.OpenGameRule;
import com.cs525.reversi.util.rules.ResultsInPointsRule;
import com.cs525.reversi.util.rules.Rule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs525.reversi.repositories.GameRepository;
import com.cs525.reversi.repositories.UserRepository;
import com.cs525.reversi.req.NewGame;

@Service
public class GameServiceImpl implements GameService {

	private final GameRepository gameRepo;
	private final UserRepository userRepo;
	private final Rule gameRule;

	public GameServiceImpl(GameRepository gameRepo, UserRepository userRepo,
						   EmptyRule emptyRule, OpenGameRule openGameRule, ResultsInPointsRule resultsInPointsRule) {
		this.gameRepo = gameRepo;
		this.userRepo = userRepo;
		this.gameRule = emptyRule;
		emptyRule.setNext(openGameRule);
		openGameRule.setNext(resultsInPointsRule);
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
					cells.add("WHITE");
				else if (i == 4 && j == 5 || i == 5 && j == 4)
					cells.add("BLACK");
				else
					cells.add("EMPTY");
			}
			// FIXME
			row.setCells(cells.stream().map(CellValue::valueOf).collect(Collectors.toList()));
			rows.add(row);
		}
		return rows;
	}

}
