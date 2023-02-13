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

package com.example.consolemania.catalog.util;

import java.text.Normalizer;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * It converts a string to a "slug".
 *
 * @see <a href="http://www.codecodex.com/wiki/Generate_a_url_slug#Java">Original implementation</a>
 */
public record Slug(String value) {

    public Slug(String value) {
        if (Objects.isNull(value)) {
            throw new IllegalArgumentException();
        }

        this.value = toSeoFriendlyString(value);
    }

    public static Slug of(String value) {
        return new Slug(value);
    }

    @Override
    public String toString() {
        return value;
    }

    private static final Pattern NON_LATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");
    private static final String SEP = "-";

    private static String toSeoFriendlyString(String str) {
        String noWhitespace = WHITESPACE.matcher(str).replaceAll(SEP);
        String normalized = Normalizer.normalize(noWhitespace, Normalizer.Form.NFD);
        return NON_LATIN.matcher(normalized).replaceAll("").toLowerCase(Locale.ENGLISH);
    }
}
