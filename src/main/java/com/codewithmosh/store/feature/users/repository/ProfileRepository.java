package com.codewithmosh.store.feature.users.repository;

import com.codewithmosh.store.feature.users.entity.Profile;
import org.springframework.data.repository.CrudRepository;

public interface ProfileRepository extends CrudRepository<Profile, Long> {
}