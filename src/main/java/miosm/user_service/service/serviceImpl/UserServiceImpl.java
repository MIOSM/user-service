package miosm.user_service.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import miosm.user_service.dto.CreateUserRequestDto;
import miosm.user_service.dto.UpdateUserRequestDto;
import miosm.user_service.dto.UserResponseDto;
import miosm.user_service.entity.User;
import miosm.user_service.mapper.CreateUserRequestMapper;
import miosm.user_service.mapper.UpdateUserRequestMapper;
import miosm.user_service.mapper.UserResponseMapper;
import miosm.user_service.repository.UserRepository;
import miosm.user_service.service.MinioService;
import miosm.user_service.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CreateUserRequestMapper createUserRequestMapper;
    private final UpdateUserRequestMapper updateUserRequestMapper;
    private final UserResponseMapper userResponseMapper;
    private final MinioService minioService;

    @Override
    public UserResponseDto findUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));
        return userResponseMapper.toDto(user);
    }

    @Override
    public UserResponseDto findUserById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
        return userResponseMapper.toDto(user);
    }

    @Override
    public UserResponseDto createUser(CreateUserRequestDto dto) {
        userRepository.findByUsername(dto.getUsername())
                .ifPresent(u -> { throw new IllegalArgumentException("User already exists: " + dto.getUsername()); });
        User user = createUserRequestMapper.toEntity(dto);

        if (user.getUsername() == null) {
            user.setUsername(dto.getUsername());
        }
        
        user.setId(dto.getId());
        User savedUser = userRepository.save(user);
        return userResponseMapper.toDto(savedUser);
    }

    @Override
    public UserResponseDto updateUser(UUID id, UpdateUserRequestDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.setUsername(dto.getUsername());
        user.setBio(dto.getBio());

        User updatedUser = userRepository.save(user);
        return userResponseMapper.toDto(updatedUser);
    }

    @Override
    public UserResponseDto uploadAvatar(UUID id, MultipartFile file) {
        try {
            log.info("Starting avatar upload for user: {}", id);
            
            if (file == null || file.isEmpty()) {
                log.warn("Avatar file is null or empty for user: {}", id);
                throw new IllegalArgumentException("Avatar file is required");
            }
            
            log.info("Found avatar file: {} (size: {} bytes)", file.getOriginalFilename(), file.getSize());
            
            User user = userRepository.findById(id)
                    .orElseThrow(() -> {
                        log.error("User not found: {}", id);
                        return new IllegalArgumentException("User not found: " + id);
                    });
            
            String avatarUrl = minioService.uploadImage(file, "avatars");
            log.info("Avatar uploaded to MinIO successfully: {}", avatarUrl);
            
            user.setAvatarUrl(avatarUrl);
            User updatedUser = userRepository.save(user);
            
            log.info("Avatar updated successfully for user: {}", id);
            return userResponseMapper.toDto(updatedUser);
        } catch (Exception e) {
            log.error("Failed to upload avatar for user {}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Failed to upload avatar: " + e.getMessage(), e);
        }
    }

    @Override
    public UserResponseDto deleteAvatar(UUID id) {
        try {
            log.info("Starting avatar deletion for user: {}", id);
            
            User user = userRepository.findById(id)
                    .orElseThrow(() -> {
                        log.error("User not found: {}", id);
                        return new IllegalArgumentException("User not found: " + id);
                    });
            
            if (user.getAvatarUrl() != null) {
                log.info("Deleting avatar from MinIO: {}", user.getAvatarUrl());
                minioService.deleteImage(user.getAvatarUrl());
                log.info("Avatar deleted from MinIO successfully");
                user.setAvatarUrl(null);
            } else {
                log.info("User {} has no avatar to delete", id);
            }
            
            User updatedUser = userRepository.save(user);
            log.info("Avatar deletion completed for user: {}", id);
            return userResponseMapper.toDto(updatedUser);
        } catch (Exception e) {
            log.error("Failed to delete avatar for user {}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Failed to delete avatar: " + e.getMessage(), e);
        }
    }

    @Override
    public UserResponseDto uploadCoverImage(UUID id, MultipartFile file) {
        try {
            log.info("Starting cover image upload for user: {}", id);
            
            if (file == null || file.isEmpty()) {
                log.warn("Cover image file is null or empty for user: {}", id);
                throw new IllegalArgumentException("Cover image file is required");
            }
            
            log.info("Found cover image file: {} (size: {} bytes)", file.getOriginalFilename(), file.getSize());
            
            User user = userRepository.findById(id)
                    .orElseThrow(() -> {
                        log.error("User not found: {}", id);
                        return new IllegalArgumentException("User not found: " + id);
                    });
            
            String coverImageUrl = minioService.uploadImage(file, "covers");
            log.info("Cover image uploaded to MinIO successfully: {}", coverImageUrl);
            
            user.setCoverImageUrl(coverImageUrl);
            User updatedUser = userRepository.save(user);
            
            log.info("Cover image updated successfully for user: {}", id);
            return userResponseMapper.toDto(updatedUser);
        } catch (Exception e) {
            log.error("Failed to upload cover image for user {}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Failed to upload cover image: " + e.getMessage(), e);
        }
    }

    @Override
    public UserResponseDto deleteCoverImage(UUID id) {
        try {
            log.info("Starting cover image deletion for user: {}", id);
            
            User user = userRepository.findById(id)
                    .orElseThrow(() -> {
                        log.error("User not found: {}", id);
                        return new IllegalArgumentException("User not found: " + id);
                    });
            
            if (user.getCoverImageUrl() != null) {
                log.info("Deleting cover image from MinIO: {}", user.getCoverImageUrl());
                minioService.deleteImage(user.getCoverImageUrl());
                log.info("Cover image deleted from MinIO successfully");
                user.setCoverImageUrl(null);
            } else {
                log.info("User {} has no cover image to delete", id);
            }
            
            User updatedUser = userRepository.save(user);
            log.info("Cover image deletion completed for user: {}", id);
            return userResponseMapper.toDto(updatedUser);
        } catch (Exception e) {
            log.error("Failed to delete cover image for user {}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Failed to delete cover image: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteUser(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
        userRepository.delete(user);
    }

    @Override
    public List<UserResponseDto> searchUsers(String query) {
        try {
            log.info("Searching users with query: {}", query);
            
            if (query == null || query.trim().isEmpty()) {
                log.warn("Empty search query provided");
                return List.of();
            }
            
            String trimmedQuery = query.trim();
            List<User> users = userRepository.searchUsersRanked(trimmedQuery);
            
            log.info("Found {} users matching query: {}", users.size(), trimmedQuery);
            
            return users.stream()
                    .map(userResponseMapper::toDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error searching users with query '{}': {}", query, e.getMessage(), e);
            throw new RuntimeException("Failed to search users: " + e.getMessage(), e);
        }
    }
}
