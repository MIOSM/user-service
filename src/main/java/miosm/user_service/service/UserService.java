package miosm.user_service.service;

import miosm.user_service.dto.CreateUserRequestDto;
import miosm.user_service.dto.UpdateUserRequestDto;
import miosm.user_service.dto.UserResponseDto;
import miosm.user_service.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface UserService {

    UserResponseDto findUserByUsername(String username);

    UserResponseDto createUser(CreateUserRequestDto dto);

    UserResponseDto updateUser(UUID id, UpdateUserRequestDto dto);

    UserResponseDto updateUserByUsername(String username, UpdateUserRequestDto dto);

    void deleteUser(UUID id);
}
