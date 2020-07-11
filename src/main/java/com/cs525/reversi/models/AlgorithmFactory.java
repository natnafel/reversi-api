package com.cs525.reversi.models;

public class AlgorithmFactory {

	public Algorithm getAlgorithm(AlgorithmType algorithType) {
		return algorithType.getAlgorithm();
	}
}
