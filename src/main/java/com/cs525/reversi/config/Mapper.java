package com.cs525.reversi.config;

import com.cs525.reversi.models.User;
import com.cs525.reversi.resp.UserResponse;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs525.reversi.models.Game;
import com.cs525.reversi.models.Move;
import com.cs525.reversi.resp.GameResponse;
import com.cs525.reversi.resp.MoveResponse;

@Component
public class Mapper {
	@Autowired
	ModelMapper mapper;

//	public MoveResponse moveModelToResponse(Move move) {
//		return mapper.map(move, MoveResponse.class);
//	}

	public GameResponse gameModelToResponse(Game game) {
		return mapper.map(game, GameResponse.class);
	}

	public UserResponse userToUserResponse(User user) {
		return mapper.map(user, UserResponse.class);
	}

}
