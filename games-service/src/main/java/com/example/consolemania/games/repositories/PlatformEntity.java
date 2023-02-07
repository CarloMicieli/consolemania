package com.example.consolemania.games.repositories;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Table("platforms")
public record PlatformEntity(
        @Id UUID platformId,
        String name,
        String slug,
        String manufacturer,
        Integer generation,
        String type,
        LocalDate release_jp,
        LocalDate release_na,
        LocalDate release_eu,
        boolean discontinued,
        BigDecimal introductoryPrice,
        Integer unitsSold,
        String media,
        String cpu,
        String memory,
        String display,
        @Version Integer version) {}
