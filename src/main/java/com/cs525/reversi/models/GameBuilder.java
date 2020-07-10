package com.cs525.reversi.models;

public interface GameBuilder {

	public GameBuilder buildPlayerOne(User playerOne);

	public GameBuilder buildPlayerTwo(User playerTwo);

	public GameBuilder buildGameStatus(GameStatus gameStatus);

	public GameBuilder buildGameUUID();

	public GameBuilder buildBoardGame();
	

	public Game getGame();
}
