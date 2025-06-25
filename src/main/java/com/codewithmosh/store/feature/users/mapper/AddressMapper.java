package com.codewithmosh.store.feature.users.mapper;


import com.codewithmosh.store.feature.users.dto.AddressDto;
import com.codewithmosh.store.feature.users.entity.Address;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface AddressMapper {
    AddressDto toDto(Address address);
    Address toEntity(AddressDto addressDto);

    @Mapping(target = "id", ignore = true)
    void updateAddress(AddressDto addressDto, @MappingTarget Address address);
}
