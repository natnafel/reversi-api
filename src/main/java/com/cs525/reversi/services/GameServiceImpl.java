package com.cs525.reversi.services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs525.reversi.config.Mapper;
import com.cs525.reversi.models.CellValue;
import com.cs525.reversi.models.Game;
import com.cs525.reversi.models.GameBuilder;
import com.cs525.reversi.models.GameStatus;
import com.cs525.reversi.models.MatrixRow;
import com.cs525.reversi.models.Move;
import com.cs525.reversi.models.MovePoint;
import com.cs525.reversi.models.ReversiGameBuilder;
import com.cs525.reversi.models.User;
import com.cs525.reversi.repositories.GameRepository;
import com.cs525.reversi.repositories.MoveRepository;
import com.cs525.reversi.repositories.UserRepository;
import com.cs525.reversi.req.NewGame;
import com.cs525.reversi.resp.GameResponse;
import com.cs525.reversi.resp.MoveResponse;
import com.cs525.reversi.util.iterators.CellIterator;
import com.cs525.reversi.util.iterators.EastIterator;
import com.cs525.reversi.util.iterators.NorthEastIterator;
import com.cs525.reversi.util.iterators.NorthIterator;
import com.cs525.reversi.util.iterators.NorthWestIterator;
import com.cs525.reversi.util.iterators.SouthEastIterator;
import com.cs525.reversi.util.iterators.SouthIterator;
import com.cs525.reversi.util.iterators.SouthWestIterator;
import com.cs525.reversi.util.iterators.WestIterator;
import com.cs525.reversi.util.rules.EmptyRule;
import com.cs525.reversi.util.rules.NewValueNotEmptyRule;
import com.cs525.reversi.util.rules.OpenGameRule;
import com.cs525.reversi.util.rules.ResultsInPointsRule;
import com.cs525.reversi.util.rules.Rule;

@Service
public class GameServiceImpl implements GameService {


	@Autowired
	private MoveRepository moveRepo;
	@Autowired
	private Mapper mapper;
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

	@Override
	public List<MoveResponse> getMoves(UUID gameuuid) {
		Game game = gameRepo.findByUuid(gameuuid);
		if(game == null) return new ArrayList<>();
		List<Move> moves = moveRepo.findByGameId(game.getId());
		List<MoveResponse> moveResponses = new ArrayList<>();
		for(Move move : moves) {
			moveResponses.add(new MoveResponse(move.getId(), move.getRoww(), move.getCol()));
		}
		return moveResponses;
	}
	
	@Override
	public GameResponse getGame(UUID uuid) {
		Game game = gameRepo.findByUuid(uuid);
		if(game == null) return null;
		Move move = moveRepo.findTopByOrderByIdDesc();
		GameResponse gameResponse = mapper.gameModelToResponse(game);
		if(move != null) gameResponse.setMoveId(move.getId());	
		return gameResponse;
	}

}
