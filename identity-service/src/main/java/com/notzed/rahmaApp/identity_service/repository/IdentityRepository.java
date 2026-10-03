package com.notzed.rahmaApp.identity_service.repository;

import com.notzed.rahmaApp.identity_service.entity.Identity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IdentityRepository extends JpaRepository<Identity, Long> {
    Boolean existsByEmail(String email);

    Optional<Identity> findByEmail(String email);

}
