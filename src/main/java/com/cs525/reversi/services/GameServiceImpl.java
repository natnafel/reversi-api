package com.cs525.reversi.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.cs525.reversi.exception.AlgorithmCodeDoesntExistException;
import com.cs525.reversi.exception.IllegalMoveException;
import com.cs525.reversi.exception.ProtocolCodeDoesntExistException;
import com.cs525.reversi.models.*;
import com.cs525.reversi.repositories.CellLocationRepository;
import com.cs525.reversi.req.AwayGameRequest;
import com.cs525.reversi.req.CellLocation;
import com.cs525.reversi.req.GameSideDesicion;
import com.cs525.reversi.resp.*;
import com.cs525.reversi.util.factory.AwayGameFactory;
import com.cs525.reversi.util.moderator.GameModerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.cs525.reversi.config.Mapper;
import com.cs525.reversi.exception.UsernameDoesNotExist;
import com.cs525.reversi.repositories.GameRepository;
import com.cs525.reversi.repositories.MoveRepository;
import com.cs525.reversi.repositories.UserRepository;
import com.cs525.reversi.req.NewGame;

@Service
public class GameServiceImpl implements GameService {

	private final MoveRepository moveRepo;
	private final Mapper mapper;
	private final GameRepository gameRepo;
	private final UserRepository userRepo;
	private final CellLocationRepository cellLocationRepo;
	private final GameModerator gameModerator;
	private final AlgorithmFactory algorithmFactory;
	private final AwayGameFactory awayGameFactory;

	@Value("${reversi.default-player.username}")
	private String defaultPlayerUsername;

	private static final String GAME_CREATED_SUCCESSFULLY_MESSAGE = "Game created successfully. Waiting for your next move.";
	private static final String MOVE_MADE_SUCCESSFULLY_MESSAGE = "Move made successfully. Waiting for your next move";
	private static final String LAST_MOVE_SUCCESSFUL_GAME_OVER = "Game over. Last move was made successfully.";
	private static final int DEFAULT_START_SCORE = 2;
	private static final AlgorithmType DEFAULT_SERVER_ALGORITHM = AlgorithmType.Gredy;

	public GameServiceImpl(GameRepository gameRepo, UserRepository userRepo, MoveRepository moveRepo, AwayGameFactory awayGameFactory,
						   Mapper mapper, GameModerator gameModerator, AlgorithmFactory algorithmFactory, CellLocationRepository cellLocationRepo) {
		this.gameRepo = gameRepo;
		this.userRepo = userRepo;
		this.gameModerator = gameModerator;
		this.moveRepo = moveRepo;
		this.cellLocationRepo = cellLocationRepo;
		this.mapper = mapper;
		this.algorithmFactory = algorithmFactory;
		this.awayGameFactory = awayGameFactory;
	}

	@Override
	public NewGameAndMoveResp createNewGame(NewGame newGameForm) {

		User player1 = getDefaultPlayer();
		User player2 = userRepo.findByUsername(newGameForm.getUserName())
				.orElseGet(() -> userRepo.save(new User(UUID.randomUUID(), newGameForm.getUserName())));

		GameBuilder reversiGameBuilder = new ReversiGameBuilder();

		Game game = reversiGameBuilder.buildPlayerOne(player1).buildPlayerTwo(player2).buildGameStatus(GameStatus.OPEN)
				.buildGameUUID().buildBoardGame().getGame();

		CellLocation serverMoveCellLocation = null;
		int homeScore = DEFAULT_START_SCORE;
		int awayScore = DEFAULT_START_SCORE;

		gameRepo.saveAndFlush(game);

		if (newGameForm.getFirstMove() == GameSideDesicion.HOME) {
			serverMoveCellLocation = makeMoveForServer(game).getCellLocation();
			homeScore = gameModerator.playerScore(game, game.getPlayer1());
			awayScore = gameModerator.playerScore(game, game.getPlayer2());
		}

		return new NewGameAndMoveResp(new Info(ResponseStatus.SUCCESSFUL, GAME_CREATED_SUCCESSFULLY_MESSAGE), game.getUuid(),
				homeScore, awayScore, serverMoveCellLocation,
				toBoard(game.getRows()));

	}

	@Override
	public List<MoveResponse> getMoves(UUID gameuuid) {
		Game game = gameRepo.findByUuid(gameuuid);
		if (game == null)
			return new ArrayList<>();
		List<Move> moves = moveRepo.findByGameId(game.getId());
		List<MoveResponse> moveResponses = new ArrayList<>();
		for (Move move : moves) {
			moveResponses.add(new MoveResponse(move.getId(), mapper.userToUserResponse(move.getPlayer()),
					move.getRoww(), move.getCol(), move.getCellsToFlip()));
		}
		return moveResponses;
	}

	@Override
	public GameResponse getGame(UUID uuid) {
		Game game = gameRepo.findByUuid(uuid);
		if (game == null)
			return null;
		Move move = moveRepo.findTopByOrderByIdDesc();
		GameResponse gameResponse = mapper.gameModelToResponse(game);
		gameResponse.setBoard(toBoard(game.getRows()));
		if (move != null)
			gameResponse.setLastMoveId(move.getId());
		return gameResponse;
	}

	@Override
	public List<LookupResp> getSupportedAlgorithms() {
		return Arrays.stream(AlgorithmType.values()).map(e -> new LookupResp(e.getName(), e.getCode()))
				.collect(Collectors.toList());
	}

	@Override
	public List<GameResponse> getAllGames() {

		List<GameResponse> gameResponses = new ArrayList<>();
		List<Game> games = gameRepo.findAll();

		for (Game game : games) {
			GameResponse gameResponse = mapper.gameModelToResponse(game);
			gameResponse.setBoard(toBoard(game.getRows()));
			gameResponse.setLastMoveId(-1); // too expensive to compute for each game
			gameResponses.add(gameResponse);
		}

		return gameResponses;
	}

	@Override
	public AwayGameResponse startAwayGame(AwayGameRequest awayGameRequest) {
		String player2Username = String.format("%s:%s", awayGameRequest.getAddress(), awayGameRequest.getPort());

		User player1 = getDefaultPlayer();
		User player2 = userRepo.findByUsername(player2Username)
				.orElseGet(() -> userRepo.save(new User(UUID.randomUUID(), player2Username)));

		Game game = new ReversiGameBuilder()
				.buildPlayerOne(player1)
				.buildPlayerTwo(player2)
				.buildGameStatus(GameStatus.OPEN)
				.buildGameUUID()
				.buildBoardGame()
				.getGame();

		gameRepo.save(game);

		playAwayGame(game, awayGameRequest);

		return new AwayGameResponse(game.getUuid());
	}

	@Override
	public NewGameAndMoveResp makeMoveForOpponent(UUID gameUUID, CellLocation newMoveLocation) {
		return makeMoveForOpponent(gameRepo.findByUuid(gameUUID), newMoveLocation);

	}

	@Override
	public NewGameAndMoveResp makeMoveForOpponent(Game game, CellLocation newMoveLocation) {
		// player 2 is client
		if (!gameModerator.validateMove(game, newMoveLocation, game.getPlayer2())) {
			throw new IllegalMoveException(newMoveLocation);
		}

		MoveScore moveScoreOpponent = gameModerator.moveScoreForNewPiece(game, newMoveLocation, game.getPlayer2());
		gameModerator.applyMove(game, moveScoreOpponent);
		cellLocationRepo.saveAll(moveScoreOpponent.getCellsToFlip());
		moveRepo.save(new Move(game, game.getPlayer2(), moveScoreOpponent.getCellLocation().getRow(),
				moveScoreOpponent.getCellLocation().getCol(), moveScoreOpponent.getCellsToFlip()));

		gameRepo.saveAndFlush(game);
		cellLocationRepo.flush();
		moveRepo.flush();

		String infoMessage = LAST_MOVE_SUCCESSFUL_GAME_OVER;
		ResponseStatus status = ResponseStatus.GAME_OVER;
		CellLocation serverMoveCellLocation = null;

		if (game.getStatus() != GameStatus.CLOSED){
			infoMessage = MOVE_MADE_SUCCESSFULLY_MESSAGE;
			status = ResponseStatus.SUCCESSFUL;
			serverMoveCellLocation = makeMoveForServer(game).getCellLocation();
		}

		return new NewGameAndMoveResp(new Info(status, infoMessage), game.getUuid(),
				gameModerator.playerScore(game, game.getPlayer1()), gameModerator.playerScore(game, game.getPlayer2()),
				serverMoveCellLocation, toBoard(game.getRows()));
	}

	@Override
	public User getDefaultPlayer() {
		return userRepo.findByUsername(defaultPlayerUsername).orElseThrow(() -> new UsernameDoesNotExist(defaultPlayerUsername));
	}

	private Algorithm getDefaultAlgorithm(){
		return algorithmFactory.getAlgorithm(DEFAULT_SERVER_ALGORITHM);
	}

	@Override
	public List<LookupResp> getSupportedProtocols() {
		return Arrays.stream(Protocol.values())
				.map(protocol -> new LookupResp(protocol.getName(), protocol.getCode()))
				.collect(Collectors.toList());
	}

	@Async
	void playAwayGame(Game game, AwayGameRequest awayGameRequest){
		awayGameFactory.getAwayGame(
				Arrays.stream(Protocol.values())
						.filter(protocol -> protocol.getCode().equals(awayGameRequest.getProtocol()))
						.findAny()
						.orElseThrow(() -> new ProtocolCodeDoesntExistException(awayGameRequest.getProtocol())))
				.play(game, awayGameRequest.getAddress(), awayGameRequest.getPort(),
						algorithmFactory.getAlgorithm(
								Arrays.stream(AlgorithmType.values())
								.filter(algorithmType -> algorithmType.getCode().equals(awayGameRequest.getAlgorithm()))
								.findAny()
								.orElseThrow(() -> new AlgorithmCodeDoesntExistException(awayGameRequest.getAlgorithm()))),
						awayGameRequest.isMakeFirstMove());
	}

	private List<List<CellValue>> toBoard(List<MatrixRow> rows) {
		return rows.stream().map((matrixRow -> new ArrayList<>(matrixRow.getCells()))).collect(Collectors.toList());
	}

	private MoveScore makeMoveForServer(Game game){
		MoveScore serverMove = gameModerator.moveByAlgorithmForUser(game,
				userRepo.findByUsername(defaultPlayerUsername).orElseThrow(() -> new UsernameDoesNotExist(defaultPlayerUsername)), getDefaultAlgorithm());

		gameModerator.applyMove(game, serverMove);

		gameRepo.save(game);
		cellLocationRepo.saveAll(serverMove.getCellsToFlip());
		moveRepo.save(new Move(game, game.getPlayer1(), serverMove.getCellLocation().getRow(), serverMove.getCellLocation().getCol(), serverMove.getCellsToFlip()));

		cellLocationRepo.flush();
		moveRepo.flush();
		gameRepo.flush();

		return serverMove;
	}

}
