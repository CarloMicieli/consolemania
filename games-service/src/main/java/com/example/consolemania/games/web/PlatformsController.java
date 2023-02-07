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

package com.example.consolemania.games.web;

import com.example.consolemania.games.domain.Game;
import com.example.consolemania.games.domain.Platform;
import com.example.consolemania.games.domain.PlatformRequest;
import com.example.consolemania.games.services.GamesService;
import com.example.consolemania.games.services.PlatformsService;
import jakarta.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/platforms")
public class PlatformsController {

    private final PlatformsService platformsService;
    private final GamesService gamesService;

    public PlatformsController(PlatformsService platformsService, GamesService gamesService) {
        this.platformsService = platformsService;
        this.gamesService = gamesService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<Void> postPlatform(@RequestBody @Valid PlatformRequest newPlatform) throws URISyntaxException {
        var newId = platformsService.add(newPlatform);
        return ResponseEntity.created(new URI("/platforms/" + newId)).build();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void putPlatform(@PathVariable String id, @RequestBody @Valid PlatformRequest platform) {}

    @GetMapping
    ResponseEntity<List<Platform>> getAllPlatforms() {
        return ResponseEntity.ok(platformsService.getAll());
    }

    @GetMapping("/{id}")
    ResponseEntity<Platform> getPlatformById(@PathVariable UUID id) {
        return platformsService.getPlatformById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound()
                .build());
    }

    @GetMapping("/{id}/games")
    ResponseEntity<List<Game>> getGamesByPlatformId(@PathVariable UUID id) {
        return ResponseEntity.ok(gamesService.getGamesByPlatform(id));
    }
}
