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

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import com.jcabi.urn.URN;
import it.consolemania.catalog.games.GameModel;
import it.consolemania.catalog.games.GamesService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    ResponseEntity<Void> postPlatform(@RequestBody @Valid PlatformRequest newPlatform) {
        var platformUrn = platformsService.createPlatform(newPlatform);
        return ResponseEntity.created(
                        linkTo(PlatformsController.class).slash(platformUrn).toUri())
                .build();
    }

    @PutMapping("/{platformUrn}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void putPlatform(@PathVariable URN platformUrn, @RequestBody @Valid PlatformRequest updatePlatform) {
        platformsService.updatePlatform(platformUrn, updatePlatform);
    }

    @GetMapping
    ResponseEntity<CollectionModel<PlatformModel>> getAllPlatforms(
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "25", required = false) int size) {
        var pageRequest = PageRequest.of(page, size);
        var platforms = StreamSupport.stream(
                        platformsService.getAllPlatforms(pageRequest).spliterator(), false)
                .map(PlatformModel::of)
                .collect(Collectors.toList());
        var model = CollectionModel.of(platforms);
        return ResponseEntity.ok(model);
    }

    @GetMapping("/{platformUrn}")
    ResponseEntity<PlatformModel> getPlatformByUrn(@PathVariable URN platformUrn) {
        return platformsService
                .getPlatformByUrn(platformUrn)
                .map(PlatformModel::of)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new PlatformNotFoundException(platformUrn));
    }

    @GetMapping("/{platformUrn}/games")
    ResponseEntity<CollectionModel<GameModel>> getGamesByPlatformUrn(
            @PathVariable URN platformUrn,
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "25", required = false) int size) {
        var pageRequest = PageRequest.of(page, size);
        var games = platformsService
                .getPlatformByUrn(platformUrn)
                .map(platform -> gamesService.getGamesByPlatform(platform.platformId(), pageRequest))
                .map(gamesIt -> StreamSupport.stream(gamesIt.spliterator(), false)
                        .map(GameModel::of)
                        .collect(Collectors.toList()))
                .orElseGet(List::of);
        var model = CollectionModel.of(games);
        return ResponseEntity.ok(model);
    }
}
