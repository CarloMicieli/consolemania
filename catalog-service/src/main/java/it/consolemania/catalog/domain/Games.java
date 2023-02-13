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

package it.consolemania.catalog.domain;

import com.jcabi.urn.URN;
import it.consolemania.catalog.util.Slug;
import java.time.LocalDate;
import java.time.Year;
import java.util.UUID;

public final class Games {

    public static final GameRequest FATAL_FURY_2_REQUEST = GameRequestBuilder.builder()
            .title("Fatal Fury 2")
            .platform("Neo Geo AES")
            .developer("SNK")
            .publisher("SNK")
            .genre(Genre.Fighting)
            .modes(Mode.SinglePlayer)
            .series("Fatal Fury")
            .release(ReleaseBuilder.builder()
                    .europe(LocalDate.of(1995, 7, 28))
                    .northAmerica(LocalDate.of(1995, 7, 28))
                    .japan(LocalDate.of(1995, 7, 28))
                    .build())
            .year(Year.of(1995))
            .build();

    public static final Game FATAL_FURY_2 = fromGameRequest(UUID.randomUUID(), FATAL_FURY_2_REQUEST, 1);

    private static Game fromGameRequest(UUID gameId, GameRequest game, Integer version) {
        var platform = Slug.of(game.platform());
        var gameTitle = Slug.of(game.title());
        var gameUrn = URN.create(String.format("urn:game:%s:%s", platform, gameTitle));

        return GameBuilder.builder()
                .gameId(gameId)
                .gameUrn(gameUrn)
                .developer(game.developer())
                .publisher(game.publisher())
                .genre(game.genre())
                .modes(game.modes())
                .series(game.series())
                .release(game.release())
                .year(game.year())
                .version(version)
                .build();
    }
}
