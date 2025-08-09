package miosm.user_service.service;

import miosm.user_service.dto.CreateUserRequestDto;
import miosm.user_service.dto.UpdateUserRequestDto;
import miosm.user_service.dto.UserResponseDto;
import miosm.user_service.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

public interface UserService {

    UserResponseDto findUserByUsername(String username);

    UserResponseDto findUserById(UUID id);

    UserResponseDto createUser(CreateUserRequestDto dto);

    UserResponseDto updateUser(UUID id, UpdateUserRequestDto dto);

    UserResponseDto uploadAvatar(UUID id, MultipartFile file);

    UserResponseDto deleteAvatar(UUID id);

    UserResponseDto uploadCoverImage(UUID id, MultipartFile file);

    UserResponseDto deleteCoverImage(UUID id);

    void deleteUser(UUID id);
}
