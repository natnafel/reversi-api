package com.cs525.reversi.models;

import org.springframework.stereotype.Component;

@Component
public class AlgorithmFactory {

	public Algorithm getAlgorithm(AlgorithmType algorithType) {
		return algorithType.getAlgorithm();
	}
}
