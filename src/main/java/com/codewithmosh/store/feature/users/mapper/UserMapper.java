package com.codewithmosh.store.feature.users.mapper;

import com.codewithmosh.store.feature.users.dto.request.CreateUserRequestDto;
import com.codewithmosh.store.feature.users.dto.request.UpdateUserRequestDto;
import com.codewithmosh.store.feature.users.dto.response.UserResponseDto;
import com.codewithmosh.store.feature.users.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponseDto toDto(User user);

    User toEntity(CreateUserRequestDto createUserRequestDto);

    void updateUser(UpdateUserRequestDto updateUserRequestDto, @MappingTarget User user);
}
