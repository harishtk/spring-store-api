package com.codewithmosh.store.repositories;

import com.codewithmosh.store.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}