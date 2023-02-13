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

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Genre {
    @JsonProperty("ACTION")
    Action,

    @JsonProperty("ACTION_ADVENTURE")
    ActionAdventure,

    @JsonProperty("ADVENTURE")
    Adventure,

    @JsonProperty("BOARD_GAME")
    BoardGame,

    @JsonProperty("CARD_GAME")
    CardGame,

    @JsonProperty("DRIVING_RACE")
    DrivingRacing,

    @JsonProperty("FIGHTING")
    Fighting,

    @JsonProperty("PLATFORMER")
    Platformer,

    @JsonProperty("PUZZLE")
    Puzzle,

    @JsonProperty("ROLE_PLAYING")
    RolePlaying,

    @JsonProperty("SPORTS")
    Sports,

    @JsonProperty("SHOOTER")
    Shooter,

    @JsonProperty("SHOOT_EM_UP")
    ShootEmUp,

    @JsonProperty("TRIVIA_BOARD_GAME")
    TriviaBoardGame
}