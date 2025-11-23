package com.example.hibernatedemo.controller;

import com.example.hibernatedemo.model.Player;
import com.example.hibernatedemo.service.PlayerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/player")
public class PlayerController {

    private static final Logger logger = LoggerFactory.getLogger(PlayerController.class);
    private final PlayerService playerService;
    private final ObjectMapper objectMapper = new ObjectMapper();


    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping
    public List<Player> getAllPlayers() {
        logger.info("Received request to GET /player/");
        return playerService.getAllPlayers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Player> getPlayerById(@PathVariable Long id) {
        logger.info("Received request to GET /player/{}", id);
        Player player = playerService.getPlayerById(id);
        logger.info("Successfully returned 200 OK for /player/{}", id);
        return ResponseEntity.ok(player);
    }

    @PostMapping
    public ResponseEntity<Player> createPlayer(@Valid @RequestBody Player player) {
        try {
            String jsonString = objectMapper.writeValueAsString(player);
            logger.info("Received request to POST /player/ with json data: {}", jsonString);
        } catch (Exception e) {
            logger.error("Received request to POST /player/ - Error converting data to JSON: {}", e.getMessage());
        }
        Player createdPlayer = playerService.createPlayer(player);
        logger.info("Successfully returned 201 CREATED for /player/{}", createdPlayer.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPlayer);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Player> updatePlayer(@PathVariable Long id, @Valid @RequestBody Player playerDetails) {
        try {
            String jsonString = objectMapper.writeValueAsString(playerDetails);
            logger.info("Received request to PUT /player/{} with json data: {}", id, jsonString);
        } catch (Exception e) {
            logger.error("Received request to PUT /player/{} - Error converting data to JSON: {}", id, e.getMessage());
        }
        Player player = playerService.updatePlayer(id, playerDetails);
        logger.info("Successfully returned 200 OK for /player/{}", id);
        return ResponseEntity.ok(player);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlayer(@PathVariable Long id) {
        logger.info("Received request to DELETE /player/{}", id);
        playerService.deletePlayer(id);
        logger.info("Successfully returned 204 NO CONTENT for /player/{}", id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/random")
    public ResponseEntity<Player> getRandomPlayer() {
        logger.info("Received request to GET /player/random");
        Player player = playerService.getRandomPlayer();
        logger.info("Successfully returned 200 OK for /player/random");
        return ResponseEntity.ok(player);
    }
}
