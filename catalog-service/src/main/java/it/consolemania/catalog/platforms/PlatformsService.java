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

import com.jcabi.urn.URN;
import it.consolemania.catalog.util.Slug;
import it.consolemania.catalog.util.UuidSource;
import java.time.Year;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.stereotype.Service;

@Service
public class PlatformsService {

    private final PlatformsRepository platforms;
    private final UuidSource uuidSource;

    public PlatformsService(PlatformsRepository platforms, UuidSource uuidSource) {
        this.platforms = platforms;
        this.uuidSource = uuidSource;
    }

    public URN createPlatform(PlatformRequest newPlatform) {
        var newId = uuidSource.generateNewId();
        var platformEntity = entityFromRequest(newPlatform, null);

        if (platforms.existsByPlatformUrn(platformEntity.platformUrn())) {
            throw new PlatformAlreadyExistsException(platformEntity.platformUrn());
        }

        platforms.save(platformEntity);
        return platformEntity.platformUrn();
    }

    public void updatePlatform(URN platformUrn, PlatformRequest platform) {
        platforms.findByPlatformUrn(platformUrn).map(existingPlatform -> {
            var entity = entityFromRequest(platform, existingPlatform);
            return platforms.save(entity);
        });
    }

    PlatformEntity entityFromRequest(PlatformRequest platform, PlatformEntity entity) {
        var platformName = Slug.of(platform.name());
        var platformUrn = URN.create(String.format("urn:platform:%s", platformName));

        var discontinuedYear = Optional.ofNullable(platform.discontinuedYear())
                .map(Year::getValue)
                .orElse(null);

        var existingPlatform = Optional.ofNullable(entity);

        return new PlatformEntity(
                existingPlatform.map(PlatformEntity::platformId).orElseGet(uuidSource::generateNewId),
                platformUrn,
                platform.name(),
                platform.manufacturer(),
                platform.generation(),
                platform.type().name(),
                platform.release(),
                discontinuedYear,
                platform.discontinued(),
                platform.introductoryPrice(),
                platform.unitsSold(),
                platform.media(),
                platform.techSpecs(),
                existingPlatform.map(PlatformEntity::createdDate).orElse(null),
                existingPlatform.map(PlatformEntity::lastModifiedDate).orElse(null),
                existingPlatform.map(PlatformEntity::version).orElse(null));
    }

    public List<Platform> getAll() {
        return StreamSupport.stream(platforms.findAll().spliterator(), false)
                .map(this::toPlatform)
                .collect(Collectors.toList());
    }

    public Optional<Platform> getPlatformByUrn(URN platformUrn) {
        return platforms.findByPlatformUrn(platformUrn).map(this::toPlatform);
    }

    private Platform toPlatform(PlatformEntity platform) {
        var discountinuedYear =
                Optional.ofNullable(platform.discontinuedYear()).map(Year::of).orElse(null);

        return new Platform(
                platform.platformId(),
                platform.platformUrn(),
                platform.name(),
                platform.manufacturer(),
                platform.generation(),
                PlatformType.valueOf(platform.type()),
                platform.release(),
                discountinuedYear,
                platform.discontinued(),
                platform.introductoryPrice(),
                platform.unitsSold(),
                platform.media(),
                platform.techSpecs(),
                platform.version());
    }
}
