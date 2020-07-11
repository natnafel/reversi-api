package com.cs525.reversi.repositories;

import com.cs525.reversi.req.CellLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CellLocationRepository extends JpaRepository<CellLocation, Integer> {
}
