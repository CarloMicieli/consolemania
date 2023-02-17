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

package it.consolemania.catalog;

import it.consolemania.catalog.games.Games;
import it.consolemania.catalog.games.GamesService;
import it.consolemania.catalog.platforms.PlatformURN;
import it.consolemania.catalog.platforms.Platforms;
import it.consolemania.catalog.platforms.PlatformsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("seed")
public class ApplicationInit implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationInit.class);
    private final PlatformsService platformsService;
    private final GamesService gamesService;

    public ApplicationInit(PlatformsService platformsService, GamesService gamesService) {
        this.platformsService = platformsService;
        this.gamesService = gamesService;
    }

    @Override
    public void run(String... args) throws Exception {
        var platformUrn = PlatformURN.of(Platforms.NEO_GEO_AES.name());
        if (platformsService.getPlatformByUrn(platformUrn).isPresent()) {
            logger.warn("There is already data in the database. Skipping the data seeding");
            return;
        }

        platformsService.createPlatform(Platforms.NEO_GEO_AES);
        logger.info("[Platform] Neo Geo AES inserted");

        gamesService.createGame(Games.FATAL_FURY_3);
        logger.info("[Game] FATAL FURY 3 inserted");
    }
}
