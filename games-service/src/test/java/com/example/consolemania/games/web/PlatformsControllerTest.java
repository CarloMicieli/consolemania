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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.consolemania.games.domain.Media;
import com.example.consolemania.games.domain.Platform;
import com.example.consolemania.games.domain.PlatformRequest;
import com.example.consolemania.games.domain.PlatformType;
import com.example.consolemania.games.domain.Release;
import com.example.consolemania.games.domain.TechSpecs;
import com.example.consolemania.games.services.GamesService;
import com.example.consolemania.games.services.PlatformsService;
import com.example.consolemania.games.util.UuidSource;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@DisplayName("PlatformsController")
@WebMvcTest(PlatformsController.class)
class PlatformsControllerTest {

    private static final UUID FIXED_UUID = UUID.randomUUID();

    @MockBean
    private PlatformsService platformsService;

    @MockBean
    private UuidSource uuidSource;

    @MockBean
    private GamesService gamesService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        when(uuidSource.generateNewId()).thenReturn(FIXED_UUID);
    }

    @Test
    @DisplayName("it should create new platforms")
    void shouldPostNewPlatforms() throws Exception {
        var request = platformRequest();

        when(platformsService.add(request)).thenReturn(FIXED_UUID);

        mockMvc.perform(post("/platforms")
                        .content(asJsonString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/platforms/" + FIXED_UUID.toString()));
    }

    @Test
    @DisplayName("it should return a BAD_REQUEST when the new platform request is not valid")
    void shouldValidateNewPlatformRequests() throws Exception {
        var request = invalidPlatformRequest();
        mockMvc.perform(post("/platforms")
                        .content(asJsonString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("it should update platforms")
    void shouldUpdatePlatforms() throws Exception {
        var id = UUID.randomUUID();
        var request = platformRequest();

        when(platformsService.getPlatformById(id)).thenReturn(Optional.of(mock(Platform.class)));

        mockMvc.perform(put("/platforms/{id}", id.toString())
                        .content(asJsonString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(platformsService).update(id, request);
    }

    @Test
    @DisplayName("it should return a BAD_REQUEST when the updated platform request is not valid")
    void shouldValidatePlatformUpdateRequests() throws Exception {
        var request = invalidPlatformRequest();
        mockMvc.perform(put("/platforms/{id}", UUID.randomUUID().toString())
                        .content(asJsonString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("it should get all platforms")
    void shouldGetAllPlatforms() throws Exception {
        mockMvc.perform(get("/platforms").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    @DisplayName("it should return 404 when there is no platform with the given id")
    void shouldReturn404ForNotFoundPlatform() throws Exception {
        mockMvc.perform(get("/platforms/{id}", UUID.randomUUID().toString()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("it should get all games by platform id")
    void shouldGetAllGamesByPlatformId() throws Exception {
        var id = UUID.randomUUID();
        mockMvc.perform(get("/platforms/{id}/games", id.toString()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(gamesService).getGamesByPlatform(id);
    }

    PlatformRequest platformRequest() {
        return new PlatformRequest(
                "Neo Geo",
                "SNK",
                4,
                PlatformType.HomeVideoGameConsole,
                new Release(LocalDate.now(), LocalDate.now(), LocalDate.now()),
                true,
                BigDecimal.valueOf(599),
                100_000,
                Media.RomCartridge,
                new TechSpecs("Motorola 68000", "512 Kb", "36000 colors", ""));
    }

    PlatformRequest invalidPlatformRequest() {
        return new PlatformRequest(
                "",
                "SNK",
                4,
                PlatformType.HomeVideoGameConsole,
                new Release(LocalDate.now(), LocalDate.now(), LocalDate.now()),
                true,
                BigDecimal.valueOf(599),
                100_000,
                Media.RomCartridge,
                new TechSpecs("Motorola 68000", "512 Kb", "36000 colors", ""));
    }

    String asJsonString(final Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
