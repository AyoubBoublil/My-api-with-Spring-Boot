package org.project.smarttrack.mapper;

import org.mapstruct.Mapper;
import org.project.smarttrack.dto.UserDto;
import org.project.smarttrack.entity.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User userDtoToUser(UserDto userDto);

    UserDto userToUserDto(User user);

    List<User> userDtosToUsers(List<UserDto> userDtos);

    List<UserDto> usersToUserDtos(List<User> users);
}
