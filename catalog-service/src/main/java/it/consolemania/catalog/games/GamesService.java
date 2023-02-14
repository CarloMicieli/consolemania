/*
 *   Copyright (c) 2022-2023 (C) Carlo Micieli
 *
 *    Licensed to the Apache Software Foundation (ASF) under one
 *    or more contributor license agreements.  See the NOTICE file
 *    distributed with this work for additional information
 *    regarding copyright ownership.  The ASF licenses this file
 *    to you under the Apache License, Version 2.0 (the
 *    "License"); you may not use this file except in compliance
 *    with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing,
 *    software distributed under the License is distributed on an
 *    "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *    KIND, either express or implied.  See the License for the
 *    specific language governing permissions and limitations
 *    under the License.
 */

package it.consolemania.catalog.games;

import com.jcabi.urn.URN;
import it.consolemania.catalog.platforms.PlatformEntity;
import it.consolemania.catalog.platforms.PlatformNotFoundException;
import it.consolemania.catalog.platforms.PlatformsRepository;
import it.consolemania.catalog.util.Slug;
import it.consolemania.catalog.util.UuidSource;
import java.time.Year;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.stereotype.Service;

@Service
public class GamesService {

    private final GamesRepository games;
    private final PlatformsRepository platforms;
    private final UuidSource uuidSource;

    public GamesService(GamesRepository games, PlatformsRepository platforms, UuidSource uuidSource) {
        this.games = games;
        this.platforms = platforms;
        this.uuidSource = uuidSource;
    }

    public URN createGame(GameRequest newGame) {
        var gameEntity = entityFromRequest(newGame, null);

        if (games.existsByGameUrn(gameEntity.gameUrn())) {
            throw new GameAlreadyExistsException(gameEntity.gameUrn());
        }

        games.save(gameEntity);
        return gameEntity.gameUrn();
    }

    public void updateGame(URN gameUrn, GameRequest game) {
        games.findByGameUrn(gameUrn).map(existingGame -> {
            var entity = entityFromRequest(game, existingGame);
            return games.save(entity);
        });
    }

    public void deleteGame(URN gameUrn) {
        if (!games.existsByGameUrn(gameUrn)) {
            throw new GameNotFoundException(gameUrn);
        }

        games.deleteByGameUrn(gameUrn);
    }

    GameEntity entityFromRequest(GameRequest game, GameEntity gameEntity) {
        var platformId = platforms
                .findByName(game.platform())
                .map(PlatformEntity::platformId)
                .orElseThrow(() -> new PlatformNotFoundException(game.platform()));

        var platform = Slug.of(game.platform());
        var gameTitle = Slug.of(game.title());
        var gameUrn = URN.create(String.format("urn:game:%s:%s", platform, gameTitle));

        var existingGame = Optional.ofNullable(gameEntity);

        return new GameEntity(
                existingGame.map(GameEntity::gameId).orElseGet(uuidSource::generateNewId),
                gameUrn,
                platformId,
                game.title(),
                game.genres(),
                game.modes(),
                game.series(),
                game.developer(),
                game.publisher(),
                game.release(),
                game.year().getValue(),
                existingGame.map(GameEntity::createdDate).orElse(null),
                existingGame.map(GameEntity::lastModifiedDate).orElse(null),
                existingGame.map(GameEntity::version).orElse(null));
    }

    public List<Game> getGamesByPlatform(UUID platformId) {
        return StreamSupport.stream(games.findAllByPlatformId(platformId).spliterator(), false)
                .map(this::toGame)
                .collect(Collectors.toList());
    }

    public Optional<Game> getGameByUrn(URN gameUrn) {
        return games.findByGameUrn(gameUrn).map(this::toGame);
    }

    private Game toGame(GameEntity gameEntity) {
        return new Game(
                gameEntity.gameUrn(),
                gameEntity.title(),
                gameEntity.genres(),
                gameEntity.modes(),
                gameEntity.series(),
                gameEntity.developer(),
                gameEntity.publisher(),
                gameEntity.release(),
                Year.of(gameEntity.year()),
                gameEntity.version());
    }
}
