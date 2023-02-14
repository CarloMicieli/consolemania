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
public class GamesApiTests {
    @LocalServerPort
    private Integer port;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
    }

    @Test
    @DisplayName("POST /games: should create a new game")
    void shouldCreateNewGamesSuccessfully() {
        given().contentType(ContentType.JSON)
                .body(
                        """
                        {
                          "title": "Fatal Fury 2",
                          "genres": ["FIGHTING"],
                          "platform": "Neo Geo AES",
                          "modes": ["SINGLE_PLAYER"],
                          "series": "Fatal fury",
                          "developer": "SNK",
                          "publisher": "SNK",
                          "release": {
                            "japan": "1990-04-26",
                            "north_america": "1990-08-22",
                            "europe": "1991-01-01"
                          },
                          "year": 1994
                        }
                        """)
                .when()
                .post("/games")
                .then()
                .statusCode(201)
                .header("Location", "/games/urn:game:neo-geo-aes:fatal-fury-2");
    }

    @Test
    @DisplayName("PUT /games/urn:game:neo-geo-aes:fatal-fury-2: should update a game")
    void shouldUpdateGamesSuccessfully() {
        given().contentType(ContentType.JSON)
                .body(
                        """
                        {
                          "title": "Fatal Fury 2",
                          "genres": ["FIGHTING"],
                          "platform": "Neo Geo AES",
                          "modes": ["SINGLE_PLAYER"],
                          "series": "Fatal fury",
                          "developer": "SNK",
                          "publisher": "SNK",
                          "release": {
                            "japan": "1990-04-26",
                            "north_america": "1990-08-22",
                            "europe": "1991-01-01"
                          },
                          "year": 1994
                        }
                        """)
                .when()
                .put("/games/urn:game:neo-geo-aes:fatal-fury-2")
                .then()
                .statusCode(204);
    }

    @Test
    @DisplayName("GET /games/urn:game:neo-geo-aes:fatal-fury-3: should get a game")
    void shouldGetGamesSuccessfully() {
        given().contentType(ContentType.JSON)
                .when()
                .get("/games/urn:game:neo-geo-aes:fatal-fury-3")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("title", is("Fatal Fury 3"));
    }

    @Test
    @DisplayName("GET /games/not-found: should respond with 404 NOT_FOUND when the game is not found")
    void shouldRespondWithNotFoundWhenThGameIsNotFound() {
        given().contentType(ContentType.JSON)
                .when()
                .get("/games/urn:game:not-found")
                .then()
                .statusCode(404)
                .contentType("application/problem+json")
                .body("title", is("The game was not found"))
                .body("instance", is("/games/urn:game:not-found"))
                .body("type", is("https://api.bookmarks.com/errors/not-found"));
    }
}
