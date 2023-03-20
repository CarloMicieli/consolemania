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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Genre {
    EDUCATIONAL("EDUCATIONAL"),
    FIGHTER("FIGHTER"),
    FIGHTING("FIGHTING"),
    FIRST_PERSON_SHOOTER("FIRST_PERSON_SHOOTER"),
    FLYING("FLYING"),
    GAMBLING("GAMBLING"),
    GAME_SHOW("GAME_SHOW"),
    GUN("GUN"),
    HACK_AND_SLASH("HACK_AND_SLASH"),
    MAHJONG("MAHJONG"),
    MATURE("MATURE"),
    MAZE("MAZE"),
    MINI_GAMES("MINI_GAMES"),
    MISCELLANEOUS("MISCELLANEOUS"),
    MULTIMEDIA("MULTIMEDIA"),
    MULTI_PLAY("MULTI_PLAY"),
    OTHER("OTHER"),
    PARTY("PARTY"),
    PINBALL("PINBALL"),
    PLATFORM("PLATFORM"),
    POOL_AND_DART("POOL_AND_DART"),
    PUZZLE("PUZZLE"),
    QUIZ("QUIZ"),
    RACING("RACING"),
    RACING_DRIVING("RACING_DRIVING"),
    RACING_MOTORBIKE("RACING_MOTORBIKE"),
    RACING_MOTORCYCLE("RACING_MOTORCYCLE"),
    RUN_AND_GUN("RUN_AND_GUN"),
    RHYTHM("RHYTHM"),
    ROLE_PLAYING("ROLE_PLAYING"),
    SHOOTER("SHOOTER"),
    SHOOT_EM_UP("SHOOT_EM_UP"),
    SIMULATION("SIMULATION"),
    SLOTS("SLOTS"),
    SPORTS("SPORTS"),
    SPORTS_BASEBALL("SPORTS_BASEBALL"),
    SPORTS_BASKETBALL("SPORTS_BASKETBALL"),
    SPORTS_BIKING("SPORTS_BIKING"),
    SPORTS_BOWLING("SPORTS_BOWLING"),
    SPORTS_BOXING("SPORTS_BOXING"),
    SPORTS_CRICKET("SPORTS_CRICKET"),
    SPORTS_FISHING("SPORTS_FISHING"),
    SPORTS_FOOTBALL("SPORTS_FOOTBALL"),
    SPORTS_FUTURISTIC("SPORTS_FUTURISTIC"),
    SPORTS_GOLF("SPORTS_GOLF"),
    SPORTS_HANDBALL("SPORTS_HANDBALL"),
    SPORTS_HOCKEY("SPORTS_HOCKEY"),
    SPORTS_HORSE_RACING("SPORTS_HORSE_RACING"),
    SPORTS_HUNTING("SPORTS_HUNTING"),
    SPORTS_OLYMPIC("SPORTS_OLYMPIC"),
    SPORTS_POOL("SPORTS_POOL"),
    SPORTS_POOL_AND_DART("SPORTS_POOL_AND_DART"),
    SPORTS_RACING("SPORTS_RACING"),
    SPORTS_RUGBY("SPORTS_RUGBY"),
    SPORTS_SKATEBOARDING("SPORTS_SKATEBOARDING"),
    SPORTS_SKATING("SPORTS_SKATING"),
    SPORTS_SKIING("SPORTS_SKIING"),
    SPORTS_SNOWBOARDING("SPORTS_SNOWBOARDING"),
    SPORTS_SOCCER("SPORTS_SOCCER"),
    SPORTS_SURFING("SPORTS_SURFING"),
    SPORTS_TENNIS("SPORTS_TENNIS"),
    SPORTS_TRACK_AND_FIELD("SPORTS_TRACK_AND_FIELD"),
    SPORTS_VOLLEYBALL("SPORTS_VOLLEYBALL"),
    SPORTS_WRESTLING("SPORTS_WRESTLING"),
    STRATEGY("STRATEGY"),
    TABLETOP("TABLETOP"),
    UTILITY("UTILITY"),
    VIRTUAL_LIFE("VIRTUAL_LIFE"),
    WATER("WATER");

    private final String value;

    Genre(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static Genre fromValue(String value) {
        for (Genre genre : Genre.values()) {
            if (genre.value.equals(value)) {
                return genre;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}
