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

package com.example.consolemania.games.services;

import com.example.consolemania.games.domain.Game;
import com.example.consolemania.games.domain.GameRequest;
import com.example.consolemania.games.domain.Genre;
import com.example.consolemania.games.domain.Mode;
import com.example.consolemania.games.domain.Release;
import com.example.consolemania.games.repositories.GameEntity;
import com.example.consolemania.games.repositories.GamesRepository;
import com.example.consolemania.games.repositories.PlatformEntity;
import com.example.consolemania.games.repositories.PlatformsRepository;
import com.example.consolemania.games.util.Slug;
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

    public GamesService(GamesRepository games, PlatformsRepository platforms) {
        this.games = games;
        this.platforms = platforms;
    }

    public UUID add(GameRequest newGame) {
        var newId = UUID.randomUUID();
        var release = Optional.ofNullable(newGame.release());

        var platformId = platforms
                .findByName(newGame.platform())
                .map(PlatformEntity::platformId)
                .orElseThrow();

        var gameEntity = new GameEntity(
                newId,
                platformId,
                new Slug(newGame.title()).value(),
                newGame.title(),
                newGame.genre().name(),
                newGame.modes().name(),
                newGame.series(),
                newGame.developer(),
                newGame.publisher(),
                release.map(Release::japan).orElse(null),
                release.map(Release::northAmerica).orElse(null),
                release.map(Release::europe).orElse(null),
                newGame.year().getValue(),
                null);

        games.save(gameEntity);
        return newId;
    }

    public void update(UUID gameId, GameRequest game) {
        var platformId = platforms
                .findByName(game.platform())
                .map(PlatformEntity::platformId)
                .orElseThrow();
        var release = Optional.ofNullable(game.release());

        var gameEntity = new GameEntity(
                gameId,
                platformId,
                new Slug(game.title()).value(),
                game.title(),
                game.genre().name(),
                game.modes().name(),
                game.series(),
                game.developer(),
                game.publisher(),
                release.map(Release::japan).orElse(null),
                release.map(Release::northAmerica).orElse(null),
                release.map(Release::europe).orElse(null),
                game.year().getValue(),
                1);

        games.save(gameEntity);
    }

    public List<Game> getGamesByPlatform(UUID platformId) {
        return StreamSupport.stream(games.findAllByPlatformId(platformId).spliterator(), false)
                .map(this::toGame)
                .collect(Collectors.toList());
    }

    public Optional<Game> getGameById(UUID gameId) {
        return games.findById(gameId).map(this::toGame);
    }

    private Game toGame(GameEntity gameEntity) {
        var release = new Release(gameEntity.release_jp(), gameEntity.release_na(), gameEntity.release_eu());

        return new Game(
                gameEntity.gameId(),
                gameEntity.slug(),
                gameEntity.title(),
                Genre.valueOf(gameEntity.genre()),
                Mode.valueOf(gameEntity.modes()),
                gameEntity.series(),
                gameEntity.developer(),
                gameEntity.publisher(),
                release,
                Year.of(gameEntity.year()));
    }
}
