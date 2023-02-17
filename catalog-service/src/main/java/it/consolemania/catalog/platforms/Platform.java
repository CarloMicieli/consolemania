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

import static org.springframework.data.relational.core.mapping.Embedded.OnEmpty.USE_NULL;

import com.jcabi.urn.URN;
import io.soabase.recordbuilder.core.RecordBuilder;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Embedded;
import org.springframework.data.relational.core.mapping.Table;

@Table("platforms")
@RecordBuilder
public record Platform(
        @Id UUID platformId,
        URN platformUrn,
        String name,
        String manufacturer,
        Integer generation,
        String type,
        @Embedded(onEmpty = USE_NULL) Release release,
        Integer discontinuedYear,
        boolean discontinued,
        BigDecimal introductoryPrice,
        Integer unitsSold,
        Media media,
        @Embedded(onEmpty = USE_NULL) TechSpecs techSpecs,
        @CreatedDate Instant createdDate,
        @LastModifiedDate Instant lastModifiedDate,
        @Version Integer version) {}
