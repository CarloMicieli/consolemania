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
import it.consolemania.catalog.platforms.Platform;
import it.consolemania.catalog.platforms.PlatformNotFoundException;
import it.consolemania.catalog.platforms.PlatformsRepository;
import it.consolemania.catalog.util.UuidSource;
import java.util.Optional;
import java.util.UUID;
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

    Game entityFromRequest(GameRequest game, Game gameEntity) {
        var platformId = platforms
                .findByName(game.platform())
                .map(Platform::platformId)
                .orElseThrow(() -> new PlatformNotFoundException(game.platform()));

        var gameUrn = GameURN.of(game.platform(), game.title());

        var existingGame = Optional.ofNullable(gameEntity);

        return new Game(
                existingGame.map(Game::gameId).orElseGet(uuidSource::generateNewId),
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
                existingGame.map(Game::createdDate).orElse(null),
                existingGame.map(Game::lastModifiedDate).orElse(null),
                existingGame.map(Game::version).orElse(null));
    }

    public Iterable<Game> getGamesByPlatform(UUID platformId) {
        return games.findAllByPlatformId(platformId);
    }

    public Optional<Game> getGameByUrn(URN gameUrn) {
        return games.findByGameUrn(gameUrn);
    }
}
