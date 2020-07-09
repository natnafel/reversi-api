package com.cs525.reversi.models;

public interface GameBuilder {

	public GameBuilder buildPlayerOne(User playerOne);

	public GameBuilder buildPlayerTwo(User playerTwo);

	public ReversiGameBuilder buildGameStatus(GameStatus gameStatus);

	public ReversiGameBuilder buildGameUUID();

	public ReversiGameBuilder buildBoardGame();
	

	public Game getGame();
}
