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

package com.example.consolemania.catalog.services;

import com.example.consolemania.catalog.domain.Game;
import com.example.consolemania.catalog.domain.GameRequest;
import com.example.consolemania.catalog.domain.PlatformNotFound;
import com.example.consolemania.catalog.domain.Release;
import com.example.consolemania.catalog.repositories.GameEntity;
import com.example.consolemania.catalog.repositories.GamesRepository;
import com.example.consolemania.catalog.repositories.PlatformEntity;
import com.example.consolemania.catalog.repositories.PlatformsRepository;
import com.example.consolemania.catalog.util.Slug;
import com.example.consolemania.catalog.util.UuidSource;
import com.jcabi.urn.URN;
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
        var newId = uuidSource.generateNewId();
        var gameEntity = entityFromRequest(newId, newGame, null);
        games.save(gameEntity);
        return gameEntity.gameUrn();
    }

    public void updateGame(UUID gameId, GameRequest game, Integer version) {
        var gameEntity = entityFromRequest(gameId, game, version + 1);
        games.save(gameEntity);
    }

    GameEntity entityFromRequest(UUID gameId, GameRequest game, Integer version) {
        var platformId = platforms
                .findByName(game.platform())
                .map(PlatformEntity::platformId)
                .orElseThrow(PlatformNotFound::new);
        var release = Optional.ofNullable(game.release());

        var platform = Slug.of(game.platform());
        var gameTitle = Slug.of(game.title());
        var gameUrn = URN.create(String.format("urn:game:%s:%s", platform, gameTitle));

        return new GameEntity(
                gameId,
                gameUrn,
                platformId,
                game.title(),
                game.genre(),
                game.modes(),
                game.series(),
                game.developer(),
                game.publisher(),
                release.map(Release::japan).orElse(null),
                release.map(Release::northAmerica).orElse(null),
                release.map(Release::europe).orElse(null),
                game.year().getValue(),
                version);
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
        var release = new Release(gameEntity.releaseJp(), gameEntity.releaseNa(), gameEntity.releaseEu());

        return new Game(
                gameEntity.gameId(),
                gameEntity.gameUrn(),
                gameEntity.title(),
                gameEntity.genre(),
                gameEntity.modes(),
                gameEntity.series(),
                gameEntity.developer(),
                gameEntity.publisher(),
                release,
                Year.of(gameEntity.year()),
                gameEntity.version());
    }
}
