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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import it.consolemania.catalog.platforms.Platform;
import it.consolemania.catalog.platforms.PlatformsRepository;
import it.consolemania.catalog.util.UuidSource;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("GamesService")
@ExtendWith(MockitoExtension.class)
class GamesServiceTest {

    @Mock
    private GamesRepository gamesRepository;

    @Mock
    private PlatformsRepository platformsRepository;

    @Mock
    private UuidSource uuidSource;

    @InjectMocks
    private GamesService gamesService;

    @Test
    @DisplayName("it should create a new game")
    void shouldCreateNewGames() {
        var platform = mock(Platform.class);
        when(uuidSource.generateNewId()).thenReturn(UUID.randomUUID());
        when(platform.platformId()).thenReturn(UUID.randomUUID());
        when(platformsRepository.findByName(Games.FATAL_FURY_2.platform())).thenReturn(Optional.of(platform));

        var result = gamesService.createGame(Games.FATAL_FURY_2);

        assertThat(result).isEqualTo(GameURN.of(Games.FATAL_FURY_2.platform(), Games.FATAL_FURY_2.title()));
        verify(gamesRepository).save(any(Game.class));
    }

    @Test
    @DisplayName("it should update games")
    void shouldUpdateGames() {
        var gameUrn = GameURN.of(Games.FATAL_FURY_2.platform(), Games.FATAL_FURY_2.title());
        when(gamesRepository.findByGameUrn(gameUrn)).thenReturn(Optional.of(mock(Game.class)));

        var platform = mock(Platform.class);
        when(platform.platformId()).thenReturn(UUID.randomUUID());
        when(platformsRepository.findByName(Games.FATAL_FURY_2.platform())).thenReturn(Optional.of(platform));

        gamesService.updateGame(gameUrn, Games.FATAL_FURY_2);

        verify(gamesRepository).save(any(Game.class));
    }
}
