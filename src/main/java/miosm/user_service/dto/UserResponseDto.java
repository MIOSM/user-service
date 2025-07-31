package miosm.user_service.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    private UUID id;
    private String username;
    private String firstName;
    private String lastName;
    private String bio;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
