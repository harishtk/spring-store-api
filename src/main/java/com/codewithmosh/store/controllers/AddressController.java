package com.codewithmosh.store.controllers;

import com.codewithmosh.store.dto.AddressDto;
import com.codewithmosh.store.mappers.AddressMapper;
import com.codewithmosh.store.repositories.AddressRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;


/**
 * REST controller for managing Address entities.
 * Provides CRUD operations for addresses.
 */
@Tag(name = "Address", description = "Address management APIs")
@AllArgsConstructor
@RestController
@RequestMapping("/addresses")
public class AddressController {

    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;

    @Operation(summary = "Get all addresses", description = "Returns a list of all addresses")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved addresses",
            content = @Content(schema = @Schema(implementation = AddressDto.class)))
    @GetMapping
    public ResponseEntity<List<AddressDto>> getAddresses() {
        return ResponseEntity.ok(
                addressRepository.findAll()
                        .stream()
                        .map(addressMapper::toDto)
                        .toList()
        );
    }

    @Operation(summary = "Get address by ID", description = "Returns a single address by its ID")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the address")
    @ApiResponse(responseCode = "404", description = "Address not found")
    @GetMapping("/{id}")
    public ResponseEntity<AddressDto> getAddressById(
            @Parameter(description = "ID of the address to retrieve") @PathVariable("id") Long id) {
        var address = addressRepository.findById(id);
        if (address.isPresent()) {
            return ResponseEntity.ok(addressMapper.toDto(address.orElseThrow()));
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Create new address", description = "Creates a new address")
    @ApiResponse(responseCode = "201", description = "Address created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input")
    @PostMapping
    public ResponseEntity<AddressDto> createAddress(
            @Parameter(description = "Address to create") @Valid @RequestBody AddressDto request,
            UriComponentsBuilder uriBuilder) {
        var address = addressMapper.toEntity(request);
        address = addressRepository.save(address);
        var location = uriBuilder.path("/addresses/{id}")
                .buildAndExpand(address.getId()).toUri();
        return ResponseEntity.created(location).body(addressMapper.toDto(address));
    }

    @Operation(summary = "Update address", description = "Updates an existing address")
    @ApiResponse(responseCode = "200", description = "Address updated successfully")
    @ApiResponse(responseCode = "404", description = "Address not found")
    @ApiResponse(responseCode = "400", description = "Invalid input")
    @PutMapping("/{id}")
    public ResponseEntity<AddressDto> updateAddress(
            @Parameter(description = "ID of the address to update") @PathVariable("id") Long id,
            @Parameter(description = "Updated address details") @Valid @RequestBody AddressDto request) {
        var address = addressRepository.findById(id);
        if (address.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        addressMapper.updateAddress(request, address.get());
        return ResponseEntity.ok(
                addressMapper.toDto(addressRepository.save(address.orElseThrow()))
        );
    }

    @Operation(summary = "Delete address", description = "Deletes an address")
    @ApiResponse(responseCode = "204", description = "Address deleted successfully")
    @ApiResponse(responseCode = "404", description = "Address not found")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddress(
            @Parameter(description = "ID of the address to delete") @PathVariable("id") Long id) {
        var address = addressRepository.findById(id);
        if (address.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        addressRepository.delete(address.orElseThrow());
        return ResponseEntity.noContent().build();
    }
}