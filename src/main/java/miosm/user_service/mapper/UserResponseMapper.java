package miosm.user_service.mapper;

import miosm.user_service.dto.UserResponseDto;
import miosm.user_service.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserResponseMapper {

    UserResponseDto toDto(User user);
}