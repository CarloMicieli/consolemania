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

package it.consolemania.catalog.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.jcabi.urn.URN;
import it.consolemania.catalog.domain.Platforms;
import it.consolemania.catalog.repositories.GamesRepository;
import it.consolemania.catalog.repositories.PlatformEntity;
import it.consolemania.catalog.repositories.PlatformsRepository;
import it.consolemania.catalog.util.UuidSource;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("PlatformsService")
@ExtendWith(MockitoExtension.class)
class PlatformsServiceTest {

    @Mock
    private GamesRepository games;

    @Mock
    private PlatformsRepository platforms;

    @Mock
    private UuidSource uuidSource;

    @InjectMocks
    private PlatformsService platformsService;

    @Test
    @DisplayName("it should create new platforms")
    void shouldCreateNewPlatforms() {
        when(uuidSource.generateNewId()).thenReturn(UUID.randomUUID());

        var result = platformsService.createPlatform(Platforms.NEO_GEO_AES_REQUEST);

        assertThat(result).isEqualTo(URN.create("urn:platform:neo-geo-aes"));
        verify(platforms).save(any(PlatformEntity.class));
    }

    @Test
    @DisplayName("it should update platforms")
    void shouldUpdatePlatforms() {
        platformsService.updatePlatform(UUID.randomUUID(), Platforms.NEO_GEO_AES_REQUEST, 42);
        verify(platforms).save(any(PlatformEntity.class));
    }
}
