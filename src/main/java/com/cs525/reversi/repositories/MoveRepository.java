package com.cs525.reversi.repositories;

import com.cs525.reversi.models.Game;
import com.cs525.reversi.models.Move;
import com.cs525.reversi.resp.MoveResponse;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MoveRepository extends JpaRepository<Move, Integer> {
	List<Move> findByGameId(int gameID);
	Move findTopByGameOrderByIdDesc(Game game);
}
