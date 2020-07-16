package com.cs525.reversi.models;

public class RandomFacotry{
	
	private RandomFacotry() {
	}

	public static RandomAlgorithm getRandomAlgorithm() {
		
		return new RandomAlgorithm();
	}

}
