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

package it.consolemania.catalog;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

@DisplayName("platforms api")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("seed")
class PlatformsApiTests {
    @LocalServerPort
    private Integer port;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
    }

    @Test
    @DisplayName("POST /platforms: should create a new platform")
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
                          "discontinued_year": 1997,
                          "discontinued": "true",
                          "introductory_price": 649,
                          "units_sold": 1000000,
                          "media": "ROM_CARTRIDGE",
                          "technical_specifications": {
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
                .statusCode(201)
                .header("Location", "http://localhost:" + port + "/platforms/urn:platform:neo-geo-mvs");
    }

    @Test
    @DisplayName("POST /platforms: should return a 409 CONFLICT when the platform already exists")
    void shouldReturn409ConflictWhenThePlatformAlreadyExists() {
        given().contentType(ContentType.JSON)
                .body(
                        """
                        {
                          "name": "Neo Geo AES",
                          "manufacturer":  "SNK",
                          "generation": 4,
                          "type": "HOME_VIDEO_GAME_CONSOLE",
                          "discontinued_year": 1997,
                          "discontinued": "true",
                          "introductory_price": 649,
                          "units_sold": 1000000,
                          "media": "ROM_CARTRIDGE"
                        }
                        """)
                .when()
                .post("/platforms")
                .then()
                .statusCode(409)
                .contentType("application/problem+json")
                .body("title", is("The platform already exists"))
                .body("instance", is("/platforms/urn:platform:neo-geo-aes"))
                .body("type", is("https://api.bookmarks.com/errors/conflict"));
    }

    @Test
    @DisplayName("PUT /platforms/urn:platform:neo-geo-aes: should update the platform")
    void shouldUpdatePlatformsSuccessfully() {
        given().contentType(ContentType.JSON)
                .body(
                        """
                        {
                          "name": "Neo Geo AES",
                          "manufacturer":  "SNK",
                          "generation": 4,
                          "type": "HOME_VIDEO_GAME_CONSOLE",
                          "release": {
                            "japan": "1990-04-26",
                            "north_america": "1990-08-22",
                            "europe": "1991-01-01"
                          },
                          "discontinued_year": 1997,
                          "discontinued": "true",
                          "introductory_price": 649,
                          "units_sold": 1000000,
                          "media": "ROM_CARTRIDGE",
                          "technical_specifications": {
                            "cpu": "Motorola 68000 @ 12MHz, Zilog Z80A @ 4MHz",
                            "memory": "64KB RAM, 84KB VRAM, 2KB Sound Memory",
                            "display": "320×224 resolution, 4096 on-screen colors out of a palette of 65536",
                            "sound": "Yamaha YM2610"
                          }
                        }
                        """)
                .when()
                .put("/platforms/urn:platform:neo-geo-mvs")
                .then()
                .statusCode(204);
    }

    @Test
    @DisplayName("GET /platforms/urn:platform:neo-geo-aes: should get the platform")
    void shouldGetPlatformsSuccessfully() {
        given().accept(ContentType.JSON)
                .when()
                .get("/platforms/urn:platform:neo-geo-aes")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("platform_urn", is("urn:platform:neo-geo-aes"))
                .body("name", is("Neo Geo AES"))
                .body("manufacturer", is("SNK Corporation"))
                .body("generation", is(4))
                .body("type", is("HOME_VIDEO_GAME_CONSOLE"))
                .body("discontinued_year", is("1997"))
                .body("discontinued", is(true))
                .body("introductory_price", is(649))
                .body("units_sold", is(100000))
                .body("media", is("ROM_CARTRIDGE"));
    }

    @Test
    @DisplayName("GET /platforms/not-found: should respond with 404 NOT_FOUND when the platform is not found")
    void shouldRespondWithNotFoundWhenThePlatformIsNotFound() {
        given().contentType(ContentType.JSON)
                .when()
                .get("/platforms/urn:platform:not-found")
                .then()
                .statusCode(404)
                .contentType("application/problem+json")
                .body("title", is("The platform was not found"))
                .body("instance", is("/platforms/urn:platform:not-found"))
                .body("type", is("https://api.bookmarks.com/errors/not-found"));
    }
}
