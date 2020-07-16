package com.cs525.reversi.models;

import org.hibernate.cfg.annotations.Nullability;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MinimaxFactory{
	
	@Autowired
	private MiniMaxBuilder miniMaxBuilder;
	
	private MinimaxFactory() {
	}
	
	public MinMaxAlgorithm getMMAlgorithm(AlgorithmMode algorithmMode) {
		if(algorithmMode == null || algorithmMode == AlgorithmMode.AB_PRUNING_WITH_HIGH_DEPTH) {
			miniMaxBuilder.depth(8).maximalWeights(AlgorithmMode.NO_WEIGHTS).pruning(true);
		}else if (algorithmMode == AlgorithmMode.AB_PRUNING_WITH_MAXIMAL_WEIGHTS) {
			miniMaxBuilder.depth(6).maximalWeights(AlgorithmMode.MAXIMAL_WEIGHTS).pruning(true);
		}else if (algorithmMode == AlgorithmMode.AB_PRUNING_WITH_MINIMAL_WEIGHTS) {
			miniMaxBuilder.depth(7).maximalWeights(AlgorithmMode.MINIMAL_WEIGHTS).pruning(true);
		}else if (algorithmMode == AlgorithmMode.NO_PRUNING_WITH_HIGH_DEPTH) {
			miniMaxBuilder.depth(7).maximalWeights(AlgorithmMode.NO_WEIGHTS).pruning(false);
		}else if (algorithmMode == AlgorithmMode.NO_PRUNING_WITH_MAXIMAL_WEIGHTS) {
			miniMaxBuilder.depth(6).maximalWeights(AlgorithmMode.MAXIMAL_WEIGHTS).pruning(false);
		}else if (algorithmMode == AlgorithmMode.NO_PRUNING_WITH_MINIMAL_WEIGHTS) {
			miniMaxBuilder.depth(6).maximalWeights(AlgorithmMode.MINIMAL_WEIGHTS).pruning(false);
		}
		return miniMaxBuilder.build();
	}
}

