package com.example.consolemania.games.services;

import com.example.consolemania.games.repositories.GamesRepository;
import com.example.consolemania.games.repositories.PlatformsRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("GamesService")
@ExtendWith(MockitoExtension.class)
class GamesServiceTest {

    @Mock
    private GamesRepository gamesRepository;
    @Mock
    private PlatformsRepository platformsRepository;

    @InjectMocks
    private GamesService gamesService;


}
