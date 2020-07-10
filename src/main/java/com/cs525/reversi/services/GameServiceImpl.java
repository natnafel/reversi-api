package com.cs525.reversi.services;

import com.cs525.reversi.exception.UsernameDoesNotExist;
import com.cs525.reversi.models.*;
import com.cs525.reversi.repositories.GameRepository;
import com.cs525.reversi.repositories.UserRepository;
import com.cs525.reversi.req.CellLocation;
import com.cs525.reversi.req.NewGame;
import com.cs525.reversi.resp.Info;
import com.cs525.reversi.resp.NewGameAndMoveResp;
import com.cs525.reversi.resp.ResponseStatus;
import com.cs525.reversi.util.iterators.*;
import com.cs525.reversi.util.rules.*;
import com.cs525.reversi.util.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.cs525.reversi.exception.UsernameDoesNotExist;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import com.cs525.reversi.config.Mapper;
import com.cs525.reversi.repositories.MoveRepository;
import com.cs525.reversi.resp.GameResponse;
import com.cs525.reversi.resp.MoveResponse;


@Service
public class GameServiceImpl implements GameService {


	@Autowired
	private MoveRepository moveRepo;
	@Autowired
	private Mapper mapper;
	private final GameRepository gameRepo;
	private final UserRepository userRepo;
	private final Rule gameRule;

	@Value("${reversi.default-player.username}")
	private String defaultPlayerUsername;

	private static final String GAME_CREATED_SUCCESSFULLY_MESSAGE = "Game created successfully";
	private static final int DEFAULT_START_SCORE = 2;

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
	public NewGameAndMoveResp createNewGame(NewGame newGameForm) {

		User player1 = userRepo.findByUsername(defaultPlayerUsername).orElseThrow(() -> new UsernameDoesNotExist(defaultPlayerUsername));
		User player2 = userRepo.findByUsername(newGameForm.getUserName()).orElseGet(() -> userRepo.save(new User(UUID.randomUUID(), newGameForm.getUserName())));

		GameBuilder reversiGameBuilder = new ReversiGameBuilder();

		Game game = reversiGameBuilder
				.buildPlayerOne(player1)
				.buildPlayerTwo(player2)
				.buildGameStatus(GameStatus.OPEN)
				.buildGameUUID()
				.buildBoardGame()
				.getGame();

		gameRepo.save(game);


		// TODO handle scenario when newGame.firstMove == HOME (API makes move) as a result board, homeTotalScore and awayTotalScore is adjusted
		return new NewGameAndMoveResp(new Info(ResponseStatus.SUCCESSFUL, GAME_CREATED_SUCCESSFULLY_MESSAGE), game.getUuid(),
				DEFAULT_START_SCORE, DEFAULT_START_SCORE, null,
				toBoard(game.getRows()));

	}

	@Override
	public List<MoveResponse> getMoves(UUID gameuuid) {
		Game game = gameRepo.findByUuid(gameuuid);
		if(game == null) return new ArrayList<>();
		List<Move> moves = moveRepo.findByGameId(game.getId());
		List<MoveResponse> moveResponses = new ArrayList<>();
		for(Move move : moves) {
			moveResponses.add(new MoveResponse(move.getId(), mapper.userToUserResponse(move.getPlayer()), move.getRoww(), move.getCol()));
		}
		return moveResponses;
	}
	
	@Override
	public GameResponse getGame(UUID uuid) {
		Game game = gameRepo.findByUuid(uuid);
		if(game == null) return null;
		Move move = moveRepo.findTopByOrderByIdDesc();
		GameResponse gameResponse = mapper.gameModelToResponse(game);
		gameResponse.setBoard(toBoard(game.getRows()));
		if(move != null) gameResponse.setLastMoveId(move.getId());
		return gameResponse;
	}

	private List<List<CellValue>> toBoard(List<MatrixRow> rows) {
		return rows.stream().map((matrixRow -> new ArrayList<>(matrixRow.getCells()))).collect(Collectors.toList());
	}

}
