package com.codewithmosh.store.feature.users.repository;

import com.codewithmosh.store.feature.users.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}