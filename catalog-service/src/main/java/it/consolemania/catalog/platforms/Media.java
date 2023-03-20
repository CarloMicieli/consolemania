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

package it.consolemania.catalog.platforms;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Media {
    CD_ROM("CD_ROM"),
    GAME_CUBE_GAME_DISC("GAME_CUBE_GAME_DISC"),
    GD_ROM("GD_ROM"),
    HU_CARD("HU_CARD"),
    MINI_CD("MINI_CD"),
    ROM_CARTRIDGE("ROM_CARTRIDGE");

    private final String value;

    Media(String value) {
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
    public static Media fromValue(String value) {
        for (Media media : Media.values()) {
            if (media.value.equals(value)) {
                return media;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}
