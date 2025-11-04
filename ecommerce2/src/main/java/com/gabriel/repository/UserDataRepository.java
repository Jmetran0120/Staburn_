package com.gabriel.repository;

import com.gabriel.entity.UserData;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserDataRepository extends CrudRepository<UserData, Integer> {
    Optional<UserData> findByEmail(String email);
    boolean existsByEmail(String email);
}

