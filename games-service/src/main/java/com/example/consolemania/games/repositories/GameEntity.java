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

package com.example.consolemania.games.repositories;

import com.example.consolemania.games.domain.Genre;
import com.example.consolemania.games.domain.Mode;
import com.jcabi.urn.URN;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Table;

@Table("games")
public record GameEntity(
        @Id UUID gameId,
        URN gameUrn,
        UUID platformId,
        String title,
        Genre genre,
        Mode modes,
        String series,
        String developer,
        String publisher,
        LocalDate releaseJp,
        LocalDate releaseNa,
        LocalDate releaseEu,
        @NotNull Integer year,
        @Version Integer version) {}
