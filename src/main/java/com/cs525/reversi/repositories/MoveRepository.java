package com.cs525.reversi.repositories;

import com.cs525.reversi.models.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MoveRepository extends JpaRepository<Game, Integer> {
}
