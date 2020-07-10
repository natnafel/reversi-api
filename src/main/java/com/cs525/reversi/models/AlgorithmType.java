package com.cs525.reversi.models;

public enum AlgorithmType {

	 Random("Random" , "RNDM" ,new RandomAlgorithm()),
	 Gredy("Greedy" ,"GRDY" , new GredyAlgorithm()),
	 MinMax("Min Max" , "MM" , new MinMaxAlgorithm());

	 private String name;
	 private String code;
	 private Algorithm algorithm;
	 
	 
	AlgorithmType(String name, String code, Algorithm algorithm) {
		this.name = name;
		this.code = code;
		this.algorithm = algorithm;
	}


	public String getName() {
		return name;
	}


	public String getCode() {
		return code;
	}


	public Algorithm getAlgorithm() {
		return algorithm;
	}
	
	
}
