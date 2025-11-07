package com.example.hibernatedemo.controller;

import com.example.hibernatedemo.exception.UserNotFoundException;
import com.example.hibernatedemo.model.User;
import com.example.hibernatedemo.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
 * Class tests for the {@link UserController} class.
 * <p>
 * Verifies the behavior of all REST endpoints exposed by the
 * {@code UserController}. It focuses on ensuring that HTTP requests are
 * correctly handled, responses have the expected status codes and body content,
 * and that the controller properly delegates to the {@link UserService} layer.
 * </p>
 */
//@ExtendWith(MockitoExtension.class)
@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
class UserControllerTest {

    private static final String USERS_ENDPOINT = "/api/users";
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private UserService userService;
    private UserController userController;
    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        user1 = new User("John", "Doe", "john.doe@example.com");
        user2 = new User("Jane", "Smith", "jane.smith@example.com");
    }

    @Test
    void getAllUsers_ShouldReturnAllUsers() throws Exception {
        List<User> users = Arrays.asList(user1, user2);
        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get(USERS_ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("John"))
                .andExpect(jsonPath("$[1].surname").value("Smith"));
    }

    @Test
    void getUserById_ShouldReturnUserById() throws Exception {
        when(userService.getUserById(1L)).thenReturn(user1);

        mockMvc.perform(get(USERS_ENDPOINT + "/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.surname").value("Doe"));
    }

    @Test
    void createUser_ShouldReturnCreatedUser() throws Exception {
        when(userService.createUser(any(User.class))).thenReturn(user1);

        mockMvc.perform(post(USERS_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.surname").value("Doe"));
    }

    @Test
    void updateUser_ShouldReturnUpdatedUser() throws Exception {
        User updatedUser = new User("Johnny", "Doe", "johnny.doe@example.com");
        when(userService.updateUser(eq(1L), any(User.class))).thenReturn(updatedUser);

        mockMvc.perform(put(USERS_ENDPOINT + "/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Johnny"));
    }

    @Test
    void deleteUser_ShouldCallUserServiceDeleteUser() throws Exception {
        doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(delete(USERS_ENDPOINT + "/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).deleteUser(1L);
    }

    // ----------- Internal Server Error Test -----------
    @Test
    void getAllUsers_KO_whenUnhandledException_thenReturns500() throws Exception {
        when(userService.getAllUsers())
                .thenThrow(new RuntimeException("Internal server error"));

        mockMvc.perform(get(USERS_ENDPOINT))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Internal server error"))
                .andExpect(jsonPath("$.details").value("Unexpected error occurred"));
    }

    // ----------- User Not Found Test -----------
    @Test
    void getUserById_KO_whenUserNotFound_thenReturns404() throws Exception {
        long missingId = 999L;
        when(userService.getUserById(missingId))
                .thenThrow(new com.example.hibernatedemo.exception.UserNotFoundException(missingId));

        mockMvc.perform(get(USERS_ENDPOINT + "/{id}", missingId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("User with id 999 not found"))
                .andExpect(jsonPath("$.details").value("The requested user does not exist"));
    }


    // ----------- Validation Error Test -----------
    @Test
    void createUser_KO_whenInvalidUser_thenReturns400AndValidationDetails() throws Exception {
        User invalidUser = new User("", "", "invalid-email");

        mockMvc.perform(post(USERS_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUser)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.details.name").value("Name cannot be blank"))
                .andExpect(jsonPath("$.details.surname").value("Surname cannot be blank"))
                .andExpect(jsonPath("$.details.email").value("Email should be valid"));
    }

    // ----------- Data Integrity Violation Test -----------
    @Test
    void createUser_KO_whenDuplicateEmail_thenReturns409() throws Exception {
        User duplicateUser = new User("Alice", "Smith", "alice@example.com");
        when(userService.createUser(any(User.class)))
                .thenThrow(new org.springframework.dao.DataIntegrityViolationException("Duplicate email"));

        mockMvc.perform(post(USERS_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(duplicateUser)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("Database constraint violation"))
                .andExpect(jsonPath("$.details").value("Database constraint violation"));
    }

    // ----------- User Not Found Test -----------
    @Test
    void updateUser_KO_NonExistent_ShouldReturnNotFound() throws Exception {
        when(userService.updateUser(eq(999L), any(User.class))).thenThrow(new UserNotFoundException(999L));

        String updatedUserJson = """
                {
                  "name": "Johnny",
                  "surname": "Doe",
                  "email": "johnny@example.com"
                }
                """;

        mockMvc.perform(put(USERS_ENDPOINT + "/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedUserJson))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.timestamp").isNotEmpty());
    }

}
