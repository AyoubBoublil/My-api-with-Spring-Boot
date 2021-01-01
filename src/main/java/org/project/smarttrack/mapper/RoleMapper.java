package org.project.smarttrack.mapper;

import org.mapstruct.Mapper;
import org.project.smarttrack.dto.RoleDto;
import org.project.smarttrack.entity.Role;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    Role roleDtoToRole(RoleDto roleDto);

    RoleDto roleToRoleDto(Role roleDto);

    List<Role> roleDtosToRoles(List<RoleDto> roleDtos);

    List<RoleDto> rolesToRoleDtos(List<Role> roles);
}
