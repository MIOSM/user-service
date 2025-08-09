package miosm.user_service.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import miosm.user_service.dto.CreateUserRequestDto;
import miosm.user_service.dto.UpdateUserRequestDto;
import miosm.user_service.dto.UserResponseDto;
import miosm.user_service.service.MinioService;
import miosm.user_service.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final MinioService minioService;

    @GetMapping("/{username}")
    public ResponseEntity<UserResponseDto> getUserByUsername(@PathVariable String username) {
        UserResponseDto user = userService.findUserByUsername(username);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable UUID id) {
        UserResponseDto user = userService.findUserById(id);
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

    @PostMapping("/{id}/avatar")
    public ResponseEntity<UserResponseDto> uploadAvatar(@PathVariable UUID id, HttpServletRequest request) {
        try {
            if (request instanceof MultipartHttpServletRequest) {
                MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
                
                for (String partName : multipartRequest.getMultiFileMap().keySet()) {
                    for (MultipartFile file : multipartRequest.getMultiFileMap().get(partName)) {
                        if (file.getOriginalFilename() != null && !file.isEmpty()) {
                            UserResponseDto updatedUser = userService.uploadAvatar(id, file);
                            return ResponseEntity.ok(updatedUser);
                        }
                    }
                }
                
                return ResponseEntity.badRequest().build();
            } else {
                return ResponseEntity.badRequest().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/{id}/coverImage")
    public ResponseEntity<UserResponseDto> uploadCoverImage(@PathVariable UUID id, HttpServletRequest request) {
        try {
            if (request instanceof MultipartHttpServletRequest) {
                MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
                
                for (String partName : multipartRequest.getMultiFileMap().keySet()) {
                    for (MultipartFile file : multipartRequest.getMultiFileMap().get(partName)) {
                        if (file.getOriginalFilename() != null && !file.isEmpty()) {
                            UserResponseDto updatedUser = userService.uploadCoverImage(id, file);
                            return ResponseEntity.ok(updatedUser);
                        }
                    }
                }
                
                return ResponseEntity.badRequest().build();
            } else {
                return ResponseEntity.badRequest().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @DeleteMapping("/{id}/avatar")
    public ResponseEntity<UserResponseDto> deleteAvatar(@PathVariable UUID id) {
        try {
            UserResponseDto updatedUser = userService.deleteAvatar(id);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}/coverImage")
    public ResponseEntity<UserResponseDto> deleteCoverImage(@PathVariable UUID id) {
        try {
            UserResponseDto updatedUser = userService.deleteCoverImage(id);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}