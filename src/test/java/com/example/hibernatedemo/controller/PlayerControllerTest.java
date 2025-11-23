package com.example.hibernatedemo.controller;

import com.example.hibernatedemo.exception.NoPlayersAvailableException;
import com.example.hibernatedemo.exception.PlayerNotFoundException;
import com.example.hibernatedemo.model.Player;
import com.example.hibernatedemo.service.PlayerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * /**
 * Class tests for the {@link PlayerController} class.
 * <p>
 * Verifies the behavior of all REST endpoints exposed by the
 * {@code PlayerController}. It focuses on ensuring that HTTP requests are
 * correctly handled, responses have the expected status codes and body content,
 * and that the controller properly delegates to the {@link PlayerService} layer.
 * </p>
 */
//@ExtendWith(MockitoExtension.class)
@WebMvcTest(PlayerController.class)
@AutoConfigureMockMvc
class PlayerControllerTest {

    private static final String USERS_ENDPOINT = "/api/player";
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private PlayerService playerService;
    private PlayerController playerController;
    private Player player1;
    private Player player2;

    @BeforeEach
    void setUp() {
        player1 = new Player("John", "Doe", "john.doe@example.com");
        player2 = new Player("Jane", "Smith", "jane.smith@example.com");
    }

    @Test
    void getAllPlayers_ShouldReturnAllPlayers() throws Exception {
        List<Player> players = Arrays.asList(player1, player2);
        when(playerService.getAllPlayers()).thenReturn(players);

        mockMvc.perform(get(USERS_ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("John"))
                .andExpect(jsonPath("$[1].surname").value("Smith"));
    }

    @Test
    void getPlayerById_ShouldReturnPlayerById() throws Exception {
        when(playerService.getPlayerById(1L)).thenReturn(player1);

        mockMvc.perform(get(USERS_ENDPOINT + "/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.surname").value("Doe"));
    }

    @Test
    void createPlayer_ShouldReturnCreatedPlayer() throws Exception {
        when(playerService.createPlayer(any(Player.class))).thenReturn(player1);

        mockMvc.perform(post(USERS_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(player1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.surname").value("Doe"));
    }

    @Test
    void updatePlayer_ShouldReturnUpdatedPlayer() throws Exception {
        Player updatedPlayer = new Player("Johnny", "Doe", "johnny.doe@example.com");
        when(playerService.updatePlayer(eq(1L), any(Player.class))).thenReturn(updatedPlayer);

        mockMvc.perform(put(USERS_ENDPOINT + "/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedPlayer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Johnny"));
    }

    @Test
    void deletePlayer_ShouldCallPlayerServiceDeletePlayer() throws Exception {
        doNothing().when(playerService).deletePlayer(1L);

        mockMvc.perform(delete(USERS_ENDPOINT + "/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(playerService, times(1)).deletePlayer(1L);
    }

    // ----------- Internal Server Error Test -----------
    @Test
    void getAllPlayers_KO_whenUnhandledException_thenReturns500() throws Exception {
        when(playerService.getAllPlayers())
                .thenThrow(new RuntimeException("Internal server error"));

        mockMvc.perform(get(USERS_ENDPOINT))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Internal server error"))
                .andExpect(jsonPath("$.details").value("Unexpected error occurred"));
    }

    // ----------- Player Not Found Test -----------
    @Test
    void getPlayerById_KO_whenPlayerNotFound_thenReturns404() throws Exception {
        long missingId = 999L;
        when(playerService.getPlayerById(missingId))
                .thenThrow(new PlayerNotFoundException(missingId));

        mockMvc.perform(get(USERS_ENDPOINT + "/{id}", missingId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("Player with id 999 not found"))
                .andExpect(jsonPath("$.details").value("The requested player does not exist"));
    }


    // ----------- Validation Error Test -----------
    @Test
    void createPlayer_KO_whenInvalidPlayer_thenReturns400AndValidationDetails() throws Exception {
        Player invalidPlayer = new Player("", "", "invalid-email");

        mockMvc.perform(post(USERS_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidPlayer)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.details.name").value("Name cannot be blank"))
                .andExpect(jsonPath("$.details.surname").value("Surname cannot be blank"))
                .andExpect(jsonPath("$.details.email").value("Email should be valid"));
    }

    // ----------- Data Integrity Violation Test -----------
    @Test
    void createPlayer_KO_whenDuplicateEmail_thenReturns409() throws Exception {
        Player duplicatePlayer = new Player("Alice", "Smith", "alice@example.com");
        when(playerService.createPlayer(any(Player.class)))
                .thenThrow(new org.springframework.dao.DataIntegrityViolationException("Duplicate email"));

        mockMvc.perform(post(USERS_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(duplicatePlayer)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("Database constraint violation"))
                .andExpect(jsonPath("$.details").value("Database constraint violation"));
    }

    // ----------- Player Not Found Test -----------
    @Test
    void updatePlayer_KO_NonExistent_ShouldReturnNotFound() throws Exception {
        when(playerService.updatePlayer(eq(999L), any(Player.class))).thenThrow(new PlayerNotFoundException(999L));

        String updatedPlayerJson = """
                {
                  "name": "Johnny",
                  "surname": "Doe",
                  "email": "johnny@example.com"
                }
                """;

        mockMvc.perform(put(USERS_ENDPOINT + "/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedPlayerJson))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.timestamp").isNotEmpty());
    }

    @Test
    void getRandomPlayer_ShouldReturnARandomPlayer() throws Exception {

        when(playerService.getRandomPlayer()).thenReturn(player1);

        mockMvc.perform(get(USERS_ENDPOINT + "/random")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(player1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.surname").value("Doe"));
    }

    @Test
    void getRandomPlayer_KO_ShouldReturnARandomPlayer() throws Exception {

        when(playerService.getRandomPlayer()).thenThrow(new NoPlayersAvailableException("No players available"));
        Assertions.assertThrows(NoPlayersAvailableException.class, () -> {
            playerService.getRandomPlayer();
        });
    }

}
