package com.codewithmosh.store.controllers;

import com.codewithmosh.store.dto.AddressDto;
import com.codewithmosh.store.mappers.AddressMapper;
import com.codewithmosh.store.repositories.AddressRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/addresses")
public class AddressController {

    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;

    @GetMapping
    public ResponseEntity<List<AddressDto>> getAddresses() {
        return ResponseEntity.ok(
                addressRepository.findAll()
                        .stream()
                        .map(addressMapper::toDto)
                        .toList()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<AddressDto> getAddressById(@PathVariable("id") Long id) {
        var address = addressRepository.findById(id);
        if (address.isPresent()) {
            return ResponseEntity.ok(addressMapper.toDto(address.orElseThrow()));
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<AddressDto> createAddress(
            @Valid @RequestBody AddressDto request,
            UriComponentsBuilder uriBuilder) {
        var address = addressMapper.toEntity(request);
        address = addressRepository.save(address);
        var location = uriBuilder.path("/addresses/{id}")
                .buildAndExpand(address.getId()).toUri();
        return ResponseEntity.created(location).body(addressMapper.toDto(address));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AddressDto> updateAddress(
            @PathVariable("id") Long id,
            @Valid @RequestBody AddressDto request) {
        var address = addressRepository.findById(id);
        if (address.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        addressMapper.updateAddress(request, address.get());
        return ResponseEntity.ok(
                addressMapper.toDto(addressRepository.save(address.orElseThrow()))
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable("id") Long id) {
        var address = addressRepository.findById(id);
        if (address.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        addressRepository.delete(address.orElseThrow());
        return ResponseEntity.noContent().build();
    }
}