package com.example.hibernatedemo.service;

import com.example.hibernatedemo.exception.UserNotFoundException;
import com.example.hibernatedemo.model.User;
import com.example.hibernatedemo.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service layer responsible for managing {@link User} entities.
 * <p>
 * This class acts as a bridge between the controller layer and the
 * {@link UserRepository}, providing high-level business logic for
 * creating, retrieving, updating, and deleting users.
 * </p>
 */
@Service
public class UserService {

    private final UserRepository userRepository;

    /**
     * Constructs a new {@code UserService} with the specified {@link UserRepository}.
     *
     * @param userRepository the repository used for performing user persistence operations
     */
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Retrieves a list of all registered users.
     *
     * @return a {@link List} containing all {@link User} objects
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Retrieves a user by their unique identifier.
     *
     * @param id the ID of the user to retrieve
     * @return the {@link User}
     * @throws UserNotFoundException if {@link User} is not found
     */
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    /**
     * Creates a new user in the system.
     *
     * @param user the {@link User} object to create
     * @return the saved {@link User} instance with a generated ID
     */
    public User createUser(User user) {
        return userRepository.save(user);
    }

    /**
     * Updates an existing user with new details.
     *
     * @param id          the ID of the user to update
     * @param updatedUser the updated {@link User} data
     * @return the updated {@link User} instance
     * @throws UserNotFoundException if {@link User} is not found
     */
    public User updateUser(Long id, User updatedUser) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        user.setName(updatedUser.getName());
        user.setSurname(updatedUser.getSurname());
        user.setEmail(updatedUser.getEmail());
        return userRepository.save(user);
    }

    /**
     * Deletes a user by their unique identifier.
     *
     * @param id the ID of the user to delete
     */
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
