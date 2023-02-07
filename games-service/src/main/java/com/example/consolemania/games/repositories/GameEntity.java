package com.example.consolemania.games.repositories;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.time.Year;
import java.util.UUID;

@Table("games")
public record GameEntity(@Id UUID gameId,
                         UUID platformId,
                         String slug,
                         String title,
                         String genre,
                         String modes,
                         String series,
                         String developer,
                         String publisher,
                         LocalDate release_jp,
                         LocalDate release_na,
                         LocalDate release_eu,
                         Year year,
                         @Version Integer version) {}
