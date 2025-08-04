package miosm.user_service.mapper;

import miosm.user_service.dto.UpdateUserRequestDto;
import miosm.user_service.entity.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface UpdateUserRequestMapper {

    void updateEntityFromDto(UpdateUserRequestDto dto, @MappingTarget User user);

    UpdateUserRequestDto toDto(User user);
}
