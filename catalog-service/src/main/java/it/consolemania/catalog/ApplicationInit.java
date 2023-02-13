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

import it.consolemania.catalog.domain.Platforms;
import it.consolemania.catalog.services.PlatformsService;
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

    public ApplicationInit(PlatformsService platformsService) {
        this.platformsService = platformsService;
    }

    @Override
    public void run(String... args) throws Exception {
        if (platformsService
                .getPlatformByUrn(Platforms.NEO_GEO_AES.platformUrn())
                .isPresent()) {
            logger.warn("There is already data in the database. Skipping the data seeding");
            return;
        }

        platformsService.createPlatform(Platforms.NEO_GEO_AES_REQUEST);
        logger.info("[Platform] Neo Geo AES inserted");
    }
}
