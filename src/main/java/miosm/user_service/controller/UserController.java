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


import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:4200")
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
    public ResponseEntity<Map<String, Object>> uploadAvatar(@PathVariable UUID id, @RequestPart("file") MultipartFile file) {
        try {
            if (file == null || file.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "No file provided"));
            }
            
            UserResponseDto updatedUser = userService.uploadAvatar(id, file);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Avatar uploaded successfully",
                "avatarUrl", updatedUser.getAvatarUrl(),
                "user", updatedUser
            ));
        } catch (Exception e) {
            log.error("Error uploading avatar for user {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("success", false, "message", "Failed to upload avatar: " + e.getMessage()));
        }
    }

    @PostMapping("/{id}/coverImage")
    public ResponseEntity<Map<String, Object>> uploadCoverImage(@PathVariable UUID id, @RequestPart("file") MultipartFile file) {
        try {
            if (file == null || file.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "No file provided"));
            }
            
            UserResponseDto updatedUser = userService.uploadCoverImage(id, file);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Cover image uploaded successfully",
                "coverImageUrl", updatedUser.getCoverImageUrl(),
                "user", updatedUser
            ));
        } catch (Exception e) {
            log.error("Error uploading cover image for user {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("success", false, "message", "Failed to upload cover image: " + e.getMessage()));
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

    @GetMapping("/search")
    public ResponseEntity<List<UserResponseDto>> searchUsers(@RequestParam String query) {
        try {
            if (query == null || query.trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            
            List<UserResponseDto> users = userService.searchUsers(query);
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            log.error("Error searching users with query '{}': {}", query, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Subscription endpoints
    @PostMapping("/{followerId}/follow/{followingId}")
    public ResponseEntity<Map<String, Object>> followUser(@PathVariable UUID followerId, @PathVariable UUID followingId) {
        try {
            userService.followUser(followerId, followingId);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "User followed successfully"
            ));
        } catch (Exception e) {
            log.error("Error following user {}: {}", followingId, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @DeleteMapping("/{followerId}/follow/{followingId}")
    public ResponseEntity<Map<String, Object>> unfollowUser(@PathVariable UUID followerId, @PathVariable UUID followingId) {
        try {
            userService.unfollowUser(followerId, followingId);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "User unfollowed successfully"
            ));
        } catch (Exception e) {
            log.error("Error unfollowing user {}: {}", followingId, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @GetMapping("/{followerId}/following/{followingId}")
    public ResponseEntity<Map<String, Object>> isFollowing(@PathVariable UUID followerId, @PathVariable UUID followingId) {
        try {
            boolean isFollowing = userService.isFollowing(followerId, followingId);
            return ResponseEntity.ok(Map.of(
                "isFollowing", isFollowing
            ));
        } catch (Exception e) {
            log.error("Error checking if user {} is following user {}: {}", followerId, followingId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("isFollowing", false));
        }
    }

    @GetMapping("/{userId}/followers")
    public ResponseEntity<List<UserResponseDto>> getFollowers(@PathVariable UUID userId) {
        try {
            List<UserResponseDto> followers = userService.getFollowers(userId);
            return ResponseEntity.ok(followers);
        } catch (Exception e) {
            log.error("Error getting followers for user {}: {}", userId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{userId}/following")
    public ResponseEntity<List<UserResponseDto>> getFollowing(@PathVariable UUID userId) {
        try {
            List<UserResponseDto> following = userService.getFollowing(userId);
            return ResponseEntity.ok(following);
        } catch (Exception e) {
            log.error("Error getting following list for user {}: {}", userId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{userId}/followers/count")
    public ResponseEntity<Map<String, Long>> getFollowersCount(@PathVariable UUID userId) {
        try {
            Long count = userService.getFollowersCount(userId);
            return ResponseEntity.ok(Map.of("count", count));
        } catch (Exception e) {
            log.error("Error getting followers count for user {}: {}", userId, e.getMessage());
            return ResponseEntity.ok(Map.of("count", 0L));
        }
    }

    @GetMapping("/{userId}/following/count")
    public ResponseEntity<Map<String, Long>> getFollowingCount(@PathVariable UUID userId) {
        try {
            Long count = userService.getFollowingCount(userId);
            return ResponseEntity.ok(Map.of("count", count));
        } catch (Exception e) {
            log.error("Error getting following count for user {}: {}", userId, e.getMessage());
            return ResponseEntity.ok(Map.of("count", 0L));
        }
    }
}