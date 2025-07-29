package miosm.user_service.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import miosm.user_service.dto.CreateUserRequestDto;
import miosm.user_service.dto.UpdateUserRequestDto;
import miosm.user_service.dto.UserResponseDto;
import miosm.user_service.entity.User;
import miosm.user_service.mapper.CreateUserRequestMapper;
import miosm.user_service.mapper.UpdateUserRequestMapper;
import miosm.user_service.mapper.UserResponseMapper;
import miosm.user_service.repository.UserRepository;
import miosm.user_service.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CreateUserRequestMapper createUserRequestMapper;
    private final UpdateUserRequestMapper updateUserRequestMapper;
    private final UserResponseMapper userResponseMapper;

    @Override
    public UserResponseDto findUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));
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

        User updatedUser = userRepository.save(user);
        return userResponseMapper.toDto(updatedUser);
    }

    @Override
    public void deleteUser(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
        userRepository.delete(user);
    }
}
