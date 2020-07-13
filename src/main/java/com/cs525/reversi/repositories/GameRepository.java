package com.cs525.reversi.repositories;

import com.cs525.reversi.models.Game;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.cs525.reversi.models.GameStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends JpaRepository<Game, Integer> {
	Game findByUuid(UUID uuid);
	List<Game> findByOrderByIdDesc();
	List<Game> findAllByUpdatedAtBeforeAndStatus(Date date, GameStatus status);
}
