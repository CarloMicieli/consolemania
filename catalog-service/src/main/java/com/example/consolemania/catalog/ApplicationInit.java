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

package com.example.consolemania.catalog;

import com.example.consolemania.catalog.repositories.GamesRepository;
import com.example.consolemania.catalog.repositories.PlatformEntity;
import com.example.consolemania.catalog.repositories.PlatformsRepository;
import com.jcabi.urn.URN;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("seed")
public class ApplicationInit implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationInit.class);

    private final GamesRepository gamesRepository;
    private final PlatformsRepository platformsRepository;

    public ApplicationInit(GamesRepository gamesRepository, PlatformsRepository platformsRepository) {
        this.gamesRepository = gamesRepository;
        this.platformsRepository = platformsRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        this.gamesRepository.deleteAll();
        this.platformsRepository.deleteAll();

        var neoGeo = new PlatformEntity(
                UUID.randomUUID(),
                URN.create("urn:platform:neo-geo-aes"),
                "Neo Geo AES",
                "SNK",
                4,
                "HOME_VIDEO_GAME_CONSOLE",
                LocalDate.parse("1990-04-26"),
                LocalDate.parse("1990-08-22"),
                LocalDate.parse("1991-01-01"),
                1997,
                true,
                BigDecimal.valueOf(649),
                1000000,
                "ROM_CARTRIDGE",
                "Motorola 68000 @ 12MHz, Zilog Z80A @ 4MHz",
                "64KB RAM, 84KB VRAM, 2KB Sound Memory",
                "320×224 resolution, 4096 on-screen colors out of a palette of 65536",
                "Yamaha YM2610",
                null);
        platformsRepository.save(neoGeo);
        logger.info("[Platform] Neo Geo AES inserted");
    }
}
