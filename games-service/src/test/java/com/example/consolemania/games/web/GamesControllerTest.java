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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.consolemania.games.domain.GameRequest;
import com.example.consolemania.games.domain.Genre;
import com.example.consolemania.games.domain.Mode;
import com.example.consolemania.games.domain.Release;
import com.example.consolemania.games.services.GamesService;
import com.example.consolemania.games.util.UuidSource;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.time.Year;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@DisplayName("GamesController")
@WebMvcTest(GamesController.class)
class GamesControllerTest {

    private static final UUID FIXED_UUID = UUID.randomUUID();

    @MockBean
    private GamesService gamesService;

    @MockBean
    private UuidSource uuidSource;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        when(uuidSource.generateNewId()).thenReturn(FIXED_UUID);
    }

    @Test
    @DisplayName("it should create new games")
    void shouldPostNewGames() throws Exception {
        var request = gameRequest();

        when(gamesService.add(request)).thenReturn(FIXED_UUID);

        mockMvc.perform(post("/games")
                        .content(asJsonString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/games/" + FIXED_UUID));
    }

    @Test
    @DisplayName("it should return a BAD_REQUEST when the new game request is not valid")
    void shouldValidateNewGameRequests() throws Exception {
        var request = invalidGameRequest();
        mockMvc.perform(post("/games")
                        .content(asJsonString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("it should update games")
    void shouldPutGames() throws Exception {
        var request = gameRequest();
        mockMvc.perform(put("/games/{id}", UUID.randomUUID().toString())
                        .content(asJsonString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("it should return a BAD_REQUEST when the updated game request is not valid")
    void shouldValidateGameUpdateRequests() throws Exception {
        var request = invalidGameRequest();
        mockMvc.perform(put("/games/{id}", UUID.randomUUID().toString())
                        .content(asJsonString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("it should return 404 when there is no game with the given id")
    void shouldGetGameById() throws Exception {
        mockMvc.perform(get("/games/{id}", UUID.randomUUID().toString()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    String asJsonString(final Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    GameRequest gameRequest() {
        return new GameRequest(
                "Fatal Fury 2",
                Genre.Fighting,
                "Neo Geo",
                Mode.SinglePlayer,
                "Fatal Fury",
                "SNK",
                "SNK",
                new Release(LocalDate.of(1995, 7, 28), LocalDate.of(1995, 7, 28), LocalDate.of(1995, 7, 28)),
                Year.of(1994));
    }

    GameRequest invalidGameRequest() {
        return new GameRequest(
                "", Genre.Fighting, "Neo Geo", Mode.SinglePlayer, "Fatal Fury", "SNK", "SNK", null, Year.of(1994));
    }
}
