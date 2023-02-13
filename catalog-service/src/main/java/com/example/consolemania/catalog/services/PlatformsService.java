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

package com.example.consolemania.catalog.services;

import com.example.consolemania.catalog.domain.Media;
import com.example.consolemania.catalog.domain.Platform;
import com.example.consolemania.catalog.domain.PlatformRequest;
import com.example.consolemania.catalog.domain.PlatformType;
import com.example.consolemania.catalog.domain.Release;
import com.example.consolemania.catalog.domain.TechSpecs;
import com.example.consolemania.catalog.repositories.PlatformEntity;
import com.example.consolemania.catalog.repositories.PlatformsRepository;
import com.example.consolemania.catalog.util.Slug;
import com.example.consolemania.catalog.util.UuidSource;
import com.jcabi.urn.URN;
import java.time.Year;
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

    public URN createPlatform(PlatformRequest newPlatform) {
        var newId = uuidSource.generateNewId();
        var platformEntity = entityFromRequest(newId, newPlatform, null);
        platformsRepository.save(platformEntity);
        return platformEntity.platformUrn();
    }

    public void updatePlatform(UUID platformId, PlatformRequest platform) {
        var platformEntity = entityFromRequest(platformId, platform, null);
        platformsRepository.save(platformEntity);
    }

    PlatformEntity entityFromRequest(UUID platformId, PlatformRequest platform, Integer version) {
        var release = Optional.ofNullable(platform.release());
        var techSpecs = Optional.ofNullable(platform.techSpecs());

        var platformName = Slug.of(platform.name());
        var platformUrn = URN.create(String.format("urn:platform:%s", platformName));

        var discontinuedYear = Optional.ofNullable(platform.discontinuedYear())
                .map(Year::getValue)
                .orElse(null);

        return new PlatformEntity(
                platformId,
                platformUrn,
                platform.name(),
                platform.manufacturer(),
                platform.generation(),
                platform.type().name(),
                release.map(Release::japan).orElse(null),
                release.map(Release::northAmerica).orElse(null),
                release.map(Release::europe).orElse(null),
                discontinuedYear,
                platform.discontinued(),
                platform.introductoryPrice(),
                platform.unitsSold(),
                platform.media().name(),
                techSpecs.map(TechSpecs::cpu).orElse(null),
                techSpecs.map(TechSpecs::memory).orElse(null),
                techSpecs.map(TechSpecs::display).orElse(null),
                techSpecs.map(TechSpecs::sound).orElse(null),
                version);
    }

    public List<Platform> getAll() {
        return StreamSupport.stream(platformsRepository.findAll().spliterator(), false)
                .map(this::toPlatform)
                .collect(Collectors.toList());
    }

    public Optional<Platform> getPlatformByUrn(URN platformUrn) {
        return platformsRepository.findByPlatformUrn(platformUrn).map(this::toPlatform);
    }

    private Platform toPlatform(PlatformEntity platform) {
        var release = new Release(platform.releaseJp(), platform.releaseNa(), platform.releaseEu());

        var techSpecs = new TechSpecs(platform.cpu(), platform.memory(), platform.display(), platform.sound());

        var discountinuedYear =
                Optional.ofNullable(platform.discontinuedYear()).map(Year::of).orElse(null);

        return new Platform(
                platform.platformId(),
                platform.platformUrn(),
                platform.name(),
                platform.manufacturer(),
                platform.generation(),
                PlatformType.valueOf(platform.type()),
                release,
                discountinuedYear,
                platform.discontinued(),
                platform.introductoryPrice(),
                platform.unitsSold(),
                Media.valueOf(platform.media()),
                techSpecs,
                platform.version());
    }
}
