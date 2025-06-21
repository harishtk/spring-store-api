package com.codewithmosh.store.mappers;

import com.codewithmosh.store.dto.request.CreateUserRequestDto;
import com.codewithmosh.store.dto.request.UpdateUserRequestDto;
import com.codewithmosh.store.dto.response.UserResponseDto;
import com.codewithmosh.store.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponseDto toDto(User user);

    User toEntity(CreateUserRequestDto createUserRequestDto);

    void updateUser(UpdateUserRequestDto updateUserRequestDto, @MappingTarget User user);
}
