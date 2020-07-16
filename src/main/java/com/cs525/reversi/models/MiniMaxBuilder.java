package com.cs525.reversi.models;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MiniMaxBuilder {
	
	@Autowired
	private MinMaxAlgorithm minMaxAlgorithm;
	

	
	
	public MiniMaxBuilder pruning(boolean isPruning) {
		minMaxAlgorithm.isPruning = isPruning;
		return this;
	}
	
	public MiniMaxBuilder depth(int depth) {
		minMaxAlgorithm.depth = depth;
		return this;
	}
	
	public MiniMaxBuilder maximalWeights(AlgorithmMode weightMode) {
		minMaxAlgorithm.weights = weightMode;
		return this;
	}
	
	public MinMaxAlgorithm build() {
		return minMaxAlgorithm;
	}

}
