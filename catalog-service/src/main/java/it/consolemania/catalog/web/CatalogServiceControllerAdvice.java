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

package it.consolemania.catalog.web;

import it.consolemania.catalog.domain.GameAlreadyExistException;
import it.consolemania.catalog.domain.PlatformAlreadyExistException;

import java.net.URI;
import java.time.Instant;
import java.util.HashMap;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CatalogServiceControllerAdvice {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ProblemDetail handleValidationExceptions(MethodArgumentNotValidException ex) {
        var errors = new HashMap<String, String>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        var problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("The request is not valid");
        problemDetail.setType(URI.create("https://api.bookmarks.com/errors/bad-request"));
        problemDetail.setProperty("errors", errors);
        return problemDetail;
    }

    @ExceptionHandler(GameAlreadyExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ProblemDetail handleGameAlreadyExistExceptions(GameAlreadyExistException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        problemDetail.setInstance(URI.create("/games/" + ex.getGameUrn()));
        problemDetail.setTitle("The game already exists");
        problemDetail.setType(URI.create("https://api.bookmarks.com/errors/conflict"));
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }

    @ExceptionHandler(PlatformAlreadyExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ProblemDetail handlePlatformAlreadyExistExceptions(PlatformAlreadyExistException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        problemDetail.setInstance(URI.create("/platforms/" + ex.getPlatformUrn()));
        problemDetail.setTitle("The platform already exists");
        problemDetail.setType(URI.create("https://api.bookmarks.com/errors/conflict"));
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }
}
