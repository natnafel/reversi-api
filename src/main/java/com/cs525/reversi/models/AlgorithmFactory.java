package com.cs525.reversi.models;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AlgorithmFactory {
	@Autowired
	MinMaxAlgorithm minMaxAlgorithm;
	
	public Algorithm getAlgorithm(AlgorithmType algorithType) {
		if(algorithType == AlgorithmType.MinMax) {
			return minMaxAlgorithm;
		}
		return algorithType.getAlgorithm();
	}
}
