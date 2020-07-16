package com.cs525.reversi.models;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AlgorithmFactory {
	@Autowired
	MinimaxFactory minimaxFactory;
	public Algorithm getAlgorithm(AlgorithmType algorithmType, AlgorithmMode algorithmMode) {
		
		if(algorithmType == AlgorithmType.MinMax) {
			
			return minimaxFactory.getMMAlgorithm(algorithmMode);
			
		}else if(algorithmType == AlgorithmType.Gredy) {
			return GreedyFacotry.getGreedyAlgorithm();
			
		} else {
			return RandomFacotry.getRandomAlgorithm();
		}
		
	}
}
	
//	@Autowired
//	MinMaxAlgorithm minMaxAlgorithm;
//	
//	public Algorithm getAlgorithm(AlgorithmType algorithType) {
//		if(algorithType == AlgorithmType.MinMax) {
//			return minMaxAlgorithm;
//		}
//		return algorithType.getAlgorithm();
//	}
//}
