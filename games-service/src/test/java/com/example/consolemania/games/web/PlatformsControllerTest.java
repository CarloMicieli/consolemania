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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.consolemania.games.domain.PlatformRelease;
import com.example.consolemania.games.domain.PlatformRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@DisplayName("PlatformsController")
@WebMvcTest(PlatformsController.class)
class PlatformsControllerTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("it should post new platforms")
    void shouldPostNewPlatforms() throws Exception {
        var request = platformRequest();
        mockMvc.perform(post("/platforms")
                        .content(asJsonString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("it should validate new platform requests")
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
    void shouldPutNewPlatforms() throws Exception {
        var request = platformRequest();
        mockMvc.perform(put("/platforms/{id}", UUID.randomUUID().toString())
                        .content(asJsonString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("it should validate platform update requests")
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
    @DisplayName("it should get platform by id")
    void shouldGetPlatformById() throws Exception {
        mockMvc.perform(get("/platforms/{id}", UUID.randomUUID().toString()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("it should get all games by platform id")
    void shouldGetAllGamesByPlatormId() throws Exception {
        mockMvc.perform(get("/platforms/{id}/games", UUID.randomUUID().toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    PlatformRequest platformRequest() {
        return new PlatformRequest(
                "Neo Geo",
                "neo-geo",
                "SNK",
                4,
                "home console",
                new PlatformRelease(LocalDate.now(), LocalDate.now(), LocalDate.now()),
                true,
                BigDecimal.valueOf(599),
                100_000,
                "ROM",
                "Motorola 68000",
                "512 Kb",
                "36000 colors");
    }

    PlatformRequest invalidPlatformRequest() {
        return new PlatformRequest(
                "",
                "neo-geo",
                "SNK",
                4,
                "home console",
                new PlatformRelease(LocalDate.now(), LocalDate.now(), LocalDate.now()),
                true,
                BigDecimal.valueOf(599),
                100_000,
                "ROM",
                "Motorola 68000",
                "512 Kb",
                "36000 colors");
    }

    String asJsonString(final Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
