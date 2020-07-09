package com.cs525.reversi.config;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs525.reversi.models.Move;
import com.cs525.reversi.resp.MoveResponse;


@Component
public class Mapper {
	@Autowired
	ModelMapper mapper;

	
	public MoveResponse moveModelToResponse(Move move) {
	return mapper.map(move, MoveResponse.class);
	}
	
	

}
