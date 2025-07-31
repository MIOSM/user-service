package miosm.user_service.controller;

import lombok.RequiredArgsConstructor;
import miosm.user_service.dto.CreateUserRequestDto;
import miosm.user_service.dto.UpdateUserRequestDto;
import miosm.user_service.dto.UserResponseDto;
import miosm.user_service.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{username}")
    public ResponseEntity<UserResponseDto> getUserByUsername(@PathVariable String username) {
        UserResponseDto user = userService.findUserByUsername(username);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/profile")
    public ResponseEntity<UserResponseDto> getUserProfile(@RequestParam String username) {
        UserResponseDto user = userService.findUserByUsername(username);
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@RequestBody CreateUserRequestDto dto) {
        UserResponseDto createdUser = userService.createUser(dto);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable UUID id, @RequestBody UpdateUserRequestDto dto) {
        UserResponseDto updatedUser = userService.updateUser(id, dto);
        return ResponseEntity.ok(updatedUser);
    }

    @PatchMapping("/update")
    public ResponseEntity<UserResponseDto> updateUserByUsername(@RequestParam String username, @RequestBody UpdateUserRequestDto dto) {
        UserResponseDto updatedUser = userService.updateUserByUsername(username, dto);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}