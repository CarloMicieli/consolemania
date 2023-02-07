package com.example.consolemania.games.repositories;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface PlatformsRepository extends CrudRepository<PlatformEntity, UUID> {
    Optional<PlatformEntity> findByName(String name);
}
