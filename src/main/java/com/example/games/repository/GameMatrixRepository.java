package com.example.games.repository;

import com.example.games.entity.GameMatrixEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameMatrixRepository extends JpaRepository<GameMatrixEntity, Long> {
}
