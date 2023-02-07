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

import com.example.consolemania.games.domain.Platform;
import com.example.consolemania.games.domain.PlatformRequest;
import com.example.consolemania.games.domain.Release;
import com.example.consolemania.games.repositories.PlatformEntity;
import com.example.consolemania.games.repositories.PlatformsRepository;
import com.example.consolemania.games.util.Slug;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class PlatformsService {

    private final PlatformsRepository platformsRepository;

    public PlatformsService(PlatformsRepository platformsRepository) {
        this.platformsRepository = platformsRepository;
    }

    public UUID add(PlatformRequest newPlatform) {
        var newId = UUID.randomUUID();

        var release = Optional.ofNullable(newPlatform.release());

        var platformEntity = new PlatformEntity(
                newId,
                newPlatform.name(),
                new Slug(newPlatform.name()).value(),
                newPlatform.manufacturer(),
                newPlatform.generation(),
                newPlatform.type(),
                release.map(Release::japan).orElse(null),
                release.map(Release::northAmerica).orElse(null),
                release.map(Release::europe).orElse(null),
                newPlatform.discontinued(),
                newPlatform.introductoryPrice(),
                newPlatform.unitsSold(),
                newPlatform.media(),
                newPlatform.cpu(),
                newPlatform.memory(),
                newPlatform.display(),
                null
        );

        platformsRepository.save(platformEntity);
        return newId;
    }

    public void update(UUID platformId, PlatformRequest platform) {
        var release = Optional.ofNullable(platform.release());

        var platformEntity = new PlatformEntity(
                platformId,
                platform.name(),
                new Slug(platform.name()).value(),
                platform.manufacturer(),
                platform.generation(),
                platform.type(),
                release.map(Release::japan).orElse(null),
                release.map(Release::northAmerica).orElse(null),
                release.map(Release::europe).orElse(null),
                platform.discontinued(),
                platform.introductoryPrice(),
                platform.unitsSold(),
                platform.media(),
                platform.cpu(),
                platform.memory(),
                platform.display(),
                1
        );

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

    private Platform toPlatform(PlatformEntity newPlatform) {
        var release = new Release(
                newPlatform.release_jp(),
                newPlatform.release_na(),
                newPlatform.release_eu()
        );

        return new Platform(
                newPlatform.platformId(),
                newPlatform.name(),
                new Slug(newPlatform.name()).value(),
                newPlatform.manufacturer(),
                newPlatform.generation(),
                newPlatform.type(),
                release,
                newPlatform.discontinued(),
                newPlatform.introductoryPrice(),
                newPlatform.unitsSold(),
                newPlatform.media(),
                newPlatform.cpu(),
                newPlatform.memory(),
                newPlatform.display()
        );
    }
}
