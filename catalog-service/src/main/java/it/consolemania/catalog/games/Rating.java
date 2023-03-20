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

package it.consolemania.catalog.games;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Rating {
    CERO_C_AGES_15_PLUS("CERO_C_AGES_15_PLUS"),
    ESRB_ADULTS_ONLY("ESRB_ADULTS_ONLY"),
    ESRB_EARLY_CHILDHOOD("ESRB_EARLY_CHILDHOOD"),
    ESRB_EVERYONE("ESRB_EVERYONE"),
    ESRB_EVERYONE_10_PLUS("ESRB_EVERYONE_10_PLUS"),
    ESRB_KIDS_TO_ADULTS("ESRB_KIDS_TO_ADULTS"),
    ESRB_MATURE("ESRB_MATURE"),
    ESRB_RATING_PENDING("ESRB_RATING_PENDING"),
    ESRB_TEEN("ESRB_TEEN"),
    HSRS_17_PLUS("HSRS_17_PLUS"),
    HSRS_ADULT("HSRS_ADULT"),
    HSRS_PARENTAL_GUIDANCE("HSRS_PARENTAL_GUIDANCE"),
    OTHER_NOT_RATED("OTHER_NOT_RATED");

    private final String value;

    Rating(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static Rating fromValue(String value) {
        for (Rating rating : Rating.values()) {
            if (rating.value.equals(value)) {
                return rating;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}
