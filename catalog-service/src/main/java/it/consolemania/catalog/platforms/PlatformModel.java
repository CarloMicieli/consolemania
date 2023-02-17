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
import java.math.BigDecimal;
import java.time.Year;
import java.util.Optional;
import java.util.UUID;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.RepresentationModel;

public final class PlatformModel extends RepresentationModel<PlatformModel> {

    private final UUID platformId;
    private final URN platformUrn;
    private final String name;
    private final String manufacturer;
    private final Integer generation;
    private final PlatformType type;
    private final Release release;
    private final Year discontinuedYear;
    private final boolean discontinued;
    private final BigDecimal introductoryPrice;
    private final Integer unitsSold;
    private final Media media;
    private final TechSpecs techSpecs;
    private final Integer version;

    private PlatformModel(
            UUID platformId,
            URN platformUrn,
            String name,
            String manufacturer,
            Integer generation,
            PlatformType type,
            Release release,
            Year discontinuedYear,
            boolean discontinued,
            BigDecimal introductoryPrice,
            Integer unitsSold,
            Media media,
            TechSpecs techSpecs,
            Integer version) {
        super(linkTo(PlatformsController.class).slash(platformUrn).withRel(IanaLinkRelations.SELF));

        this.platformId = platformId;
        this.platformUrn = platformUrn;
        this.name = name;
        this.manufacturer = manufacturer;
        this.generation = generation;
        this.type = type;
        this.release = release;
        this.discontinuedYear = discontinuedYear;
        this.discontinued = discontinued;
        this.introductoryPrice = introductoryPrice;
        this.unitsSold = unitsSold;
        this.media = media;
        this.techSpecs = techSpecs;
        this.version = version;
    }

    public UUID getPlatformId() {
        return platformId;
    }

    public URN getPlatformUrn() {
        return platformUrn;
    }

    public String getName() {
        return name;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public Integer getGeneration() {
        return generation;
    }

    public PlatformType getType() {
        return type;
    }

    public Release getRelease() {
        return release;
    }

    public Year getDiscontinuedYear() {
        return discontinuedYear;
    }

    public boolean isDiscontinued() {
        return discontinued;
    }

    public BigDecimal getIntroductoryPrice() {
        return introductoryPrice;
    }

    public Integer getUnitsSold() {
        return unitsSold;
    }

    public Media getMedia() {
        return media;
    }

    public TechSpecs getTechSpecs() {
        return techSpecs;
    }

    public Integer getVersion() {
        return version;
    }

    public static PlatformModel of(Platform platform) {
        var discountinuedYear =
                Optional.ofNullable(platform.discontinuedYear()).map(Year::of).orElse(null);

        return new PlatformModel(
                platform.platformId(),
                platform.platformUrn(),
                platform.name(),
                platform.manufacturer(),
                platform.generation(),
                PlatformType.valueOf(platform.type()),
                platform.release(),
                discountinuedYear,
                platform.discontinued(),
                platform.introductoryPrice(),
                platform.unitsSold(),
                platform.media(),
                platform.techSpecs(),
                platform.version());
    }
}
