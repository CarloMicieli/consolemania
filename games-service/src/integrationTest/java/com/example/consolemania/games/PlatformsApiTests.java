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

package com.example.consolemania.games;

import static io.restassured.RestAssured.given;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("seed")
public class PlatformsApiTests {
    @LocalServerPort
    private Integer port;

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15.1-alpine");

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
    }

    @Test
    void shouldCreateNewPlatformsSuccessfully() {
        given().contentType(ContentType.JSON)
                .body(
                        """
                        {
                          "name": "Neo Geo MVS",
                          "manufacturer":  "SNK",
                          "generation": 4,
                          "type": "HOME_VIDEO_GAME_CONSOLE",
                          "release": {
                            "japan": "1990-04-26",
                            "north_america": "1990-08-22",
                            "europe": "1991-01-01"
                          },
                          "discontinued": "true",
                          "introductory_price": 649,
                          "units_sold": 1000000,
                          "media": "ROM_CARTRIDGE",
                          "tech_specs": {
                            "cpu": "Motorola 68000 @ 12MHz, Zilog Z80A @ 4MHz",
                            "memory": "64KB RAM, 84KB VRAM, 2KB Sound Memory",
                            "display": "320×224 resolution, 4096 on-screen colors out of a palette of 65536",
                            "sound": "Yamaha YM2610"
                          }
                        }
                        """)
                .when()
                .post("/platforms")
                .then()
                .statusCode(201);
    }
}
