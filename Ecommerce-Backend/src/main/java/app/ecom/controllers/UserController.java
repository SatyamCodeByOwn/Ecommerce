package app.ecom.controllers;

import app.ecom.dto.request_dto.UserRequestDTO;
import app.ecom.dto.response_dto.UserResponseDTO;
import app.ecom.services.UserService; // You will need to create this service
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerUser(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        UserResponseDTO createdUser = userService.registerUser(userRequestDTO);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

//    /**
//     * Endpoint to retrieve a user by their unique ID.
//     *
//     * @param id The ID of the user to retrieve.
//     * @return A ResponseEntity containing the UserResponseDTO.
//     */
//    @GetMapping("/{id}")
//    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable int id) {
//        UserResponseDTO user = userService.getUserById(id);
//        return ResponseEntity.ok(user);
//    }
//
//    /**
//     * Endpoint to retrieve all users.
//     *
//     * @return A ResponseEntity containing a list of all UserResponseDTOs.
//     */
//    @GetMapping
//    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
//        List<UserResponseDTO> users = userService.getAllUsers();
//        return ResponseEntity.ok(users);
//    }
}