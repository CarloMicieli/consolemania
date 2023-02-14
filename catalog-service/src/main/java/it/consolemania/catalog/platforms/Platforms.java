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

import com.jcabi.urn.URN;
import it.consolemania.catalog.util.Slug;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Year;

public final class Platforms {

    public static final PlatformRequest NEO_GEO_AES_REQUEST = PlatformRequestBuilder.builder()
            .name("Neo Geo AES")
            .generation(4)
            .manufacturer("SNK Corporation")
            .type(PlatformType.HOME_VIDEO_GAME_CONSOLE)
            .media(Media.ROM_CARTRIDGE)
            .release(ReleaseBuilder.builder()
                    .japan(LocalDate.of(1990, 4, 26))
                    .northAmerica(LocalDate.of(1990, 8, 22))
                    .northAmerica(LocalDate.of(1991, 1, 1))
                    .build())
            .introductoryPrice(BigDecimal.valueOf(649))
            .unitsSold(100_000)
            .discontinuedYear(Year.of(1997))
            .discontinued(true)
            .techSpecs(TechSpecsBuilder.builder()
                    .cpu("Motorola 68000 @ 12MHz, Zilog Z80A @ 4MHz")
                    .memory("64KB RAM, 84KB VRAM, 2KB Sound Memory")
                    .display("320×224 resolution, 4096 on-screen colors out of a palette of 65536")
                    .sound("Yamaha YM2610")
                    .build())
            .build();

    public static final Platform NEO_GEO_AES = fromRequest(NEO_GEO_AES_REQUEST);

    private static Platform fromRequest(PlatformRequest platform) {
        var platformName = Slug.of(platform.name());
        var platformUrn = URN.create(String.format("urn:platform:%s", platformName));

        return PlatformBuilder.builder()
                .platformUrn(platformUrn)
                .name(platform.name())
                .generation(platform.generation())
                .manufacturer(platform.manufacturer())
                .type(platform.type())
                .media(platform.media())
                .release(platform.release())
                .introductoryPrice(platform.introductoryPrice())
                .unitsSold(platform.unitsSold())
                .discontinuedYear(platform.discontinuedYear())
                .discontinued(platform.discontinued())
                .techSpecs(platform.techSpecs())
                .version(1)
                .build();
    }
}
