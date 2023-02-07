package com.example.consolemania.games.repositories;

import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface GamesRepository extends CrudRepository<GameEntity, UUID> {
    Iterable<GameEntity> findAllByPlatformId(UUID platformId);
}
