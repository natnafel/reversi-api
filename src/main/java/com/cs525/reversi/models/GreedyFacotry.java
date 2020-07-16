package com.cs525.reversi.models;

public class GreedyFacotry{

	private GreedyFacotry() {
	}
	
	public static GredyAlgorithm getGreedyAlgorithm() {
			
			return new GredyAlgorithm();
		}

}
