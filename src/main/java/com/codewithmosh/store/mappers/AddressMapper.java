package com.codewithmosh.store.mappers;


import com.codewithmosh.store.dto.AddressDto;
import com.codewithmosh.store.entities.Address;
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
