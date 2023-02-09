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

import com.example.consolemania.games.domain.Media;
import com.example.consolemania.games.domain.Platform;
import com.example.consolemania.games.domain.PlatformRequest;
import com.example.consolemania.games.domain.PlatformType;
import com.example.consolemania.games.domain.Release;
import com.example.consolemania.games.domain.TechSpecs;
import com.example.consolemania.games.repositories.PlatformEntity;
import com.example.consolemania.games.repositories.PlatformsRepository;
import com.example.consolemania.games.util.Slug;
import com.example.consolemania.games.util.UuidSource;
import com.jcabi.urn.URN;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.stereotype.Service;

@Service
public class PlatformsService {

    private final PlatformsRepository platformsRepository;
    private final UuidSource uuidSource;

    public PlatformsService(PlatformsRepository platformsRepository, UuidSource uuidSource) {
        this.platformsRepository = platformsRepository;
        this.uuidSource = uuidSource;
    }

    public UUID add(PlatformRequest newPlatform) {
        var newId = uuidSource.generateNewId();

        var release = Optional.ofNullable(newPlatform.release());
        var techSpecs = Optional.ofNullable(newPlatform.techSpecs());

        var platformEntity = new PlatformEntity(
                newId,
                buildURN(new Slug(newPlatform.name()).value()).toString(),
                newPlatform.name(),
                newPlatform.manufacturer(),
                newPlatform.generation(),
                newPlatform.type().name(),
                release.map(Release::japan).orElse(null),
                release.map(Release::northAmerica).orElse(null),
                release.map(Release::europe).orElse(null),
                newPlatform.discontinued(),
                newPlatform.introductoryPrice(),
                newPlatform.unitsSold(),
                newPlatform.media().name(),
                techSpecs.map(TechSpecs::cpu).orElse(null),
                techSpecs.map(TechSpecs::memory).orElse(null),
                techSpecs.map(TechSpecs::display).orElse(null),
                techSpecs.map(TechSpecs::sound).orElse(null),
                null);

        platformsRepository.save(platformEntity);
        return newId;
    }

    public void update(UUID platformId, PlatformRequest platform) {
        var release = Optional.ofNullable(platform.release());
        var techSpecs = Optional.ofNullable(platform.techSpecs());

        var platformEntity = new PlatformEntity(
                platformId,
                buildURN(new Slug(platform.name()).value()).toString(),
                platform.name(),
                platform.manufacturer(),
                platform.generation(),
                platform.type().name(),
                release.map(Release::japan).orElse(null),
                release.map(Release::northAmerica).orElse(null),
                release.map(Release::europe).orElse(null),
                platform.discontinued(),
                platform.introductoryPrice(),
                platform.unitsSold(),
                platform.media().name(),
                techSpecs.map(TechSpecs::cpu).orElse(null),
                techSpecs.map(TechSpecs::memory).orElse(null),
                techSpecs.map(TechSpecs::display).orElse(null),
                techSpecs.map(TechSpecs::sound).orElse(null),
                null);

        platformsRepository.save(platformEntity);
    }

    public List<Platform> getAll() {
        return StreamSupport.stream(platformsRepository.findAll().spliterator(), false)
                .map(this::toPlatform)
                .collect(Collectors.toList());
    }

    public Optional<Platform> getPlatformById(UUID platformId) {
        return platformsRepository.findById(platformId).map(this::toPlatform);
    }

    private Platform toPlatform(PlatformEntity platform) {
        var release = new Release(platform.release_jp(), platform.release_na(), platform.release_eu());

        var techSpecs = new TechSpecs(platform.cpu(), platform.memory(), platform.display(), platform.sound());

        return new Platform(
                platform.platformId(),
                toURN(platform.platformUrn()),
                platform.name(),
                platform.manufacturer(),
                platform.generation(),
                PlatformType.valueOf(platform.type()),
                release,
                platform.discontinued(),
                platform.introductoryPrice(),
                platform.unitsSold(),
                Media.valueOf(platform.media()),
                techSpecs);
    }

    URN buildURN(String platform) {
        try {
            return new URN(String.format("urn:platform:%s", platform));
        } catch (URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    URN toURN(String stringURN) {
        try {
            return new URN(stringURN);
        } catch (URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }
}
