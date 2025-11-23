package com.example.hibernatedemo.service;

import com.example.hibernatedemo.exception.NoPlayersAvailableException;
import com.example.hibernatedemo.exception.PlayerNotFoundException;
import com.example.hibernatedemo.model.Player;
import com.example.hibernatedemo.repository.PlayerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Service layer responsible for managing {@link Player} entities.
 * <p>
 * This class acts as a bridge between the controller layer and the
 * {@link PlayerRepository}, providing high-level business logic for
 * creating, retrieving, updating, and deleting players.
 * </p>
 */
@Service
public class PlayerService {

    private static final Logger logger = LoggerFactory.getLogger(PlayerService.class);
    private final PlayerRepository playerRepository;

    /**
     * Constructs a new {@code PlayerService} with the specified {@link PlayerRepository}.
     *
     * @param playerRepository the repository used for performing player persistence operations
     */
    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    /**
     * Retrieves a list of all registered players.
     *
     * @return a {@link List} containing all {@link Player} objects
     */
    public List<Player> getAllPlayers() {
        logger.info("");
        return playerRepository.findAll();
    }

    /**
     * Retrieves a player by their unique identifier.
     *
     * @param id the ID of the player to retrieve
     * @return the {@link Player}
     * @throws PlayerNotFoundException if {@link Player} is not found
     */
    public Player getPlayerById(Long id) {
        logger.info("id: {}", id);
        return playerRepository.findById(id).orElseThrow(() -> new PlayerNotFoundException(id));
    }

    /**
     * Creates a new player in the system.
     *
     * @param player the {@link Player} object to create
     * @return the saved {@link Player} instance with a generated ID
     */
    public Player createPlayer(Player player) {
        logger.info("player data: {}", player.toString());
        return playerRepository.save(player);
    }

    /**
     * Updates an existing player with new details.
     *
     * @param id            the ID of the player to update
     * @param updatedPlayer the updated {@link Player} data
     * @return the updated {@link Player} instance
     * @throws PlayerNotFoundException if {@link Player} is not found
     */
    public Player updatePlayer(Long id, Player updatedPlayer) {
        logger.info("id:{} player new data: {}", id, updatedPlayer.toString());
        Player player = playerRepository.findById(id).orElseThrow(() -> new PlayerNotFoundException(id));
        player.setName(updatedPlayer.getName());
        player.setSurname(updatedPlayer.getSurname());
        player.setEmail(updatedPlayer.getEmail());
        return playerRepository.save(player);
    }

    /**
     * Deletes a player by their unique identifier.
     *
     * @param id the ID of the player to delete
     */
    public void deletePlayer(Long id) {
        logger.info("id: {}", id);
        playerRepository.deleteById(id);
    }

    public Player getRandomPlayer() {

        long count = playerRepository.count();
        if (count == 0) {
            throw new NoPlayersAvailableException("No players available");

        }

        int index = ThreadLocalRandom.current().nextInt((int) count);
        Page<Player> page = playerRepository.findAll(PageRequest.of(index, 1));
        return page.getContent().get(0);
    }

}
