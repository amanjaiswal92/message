package com.aliens.msg.repositories;

import com.aliens.msg.models.KeycloakUser;

import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the KeycloakUser entity.
 */
@SuppressWarnings("unused")
public interface KeycloakUserRepository extends JpaRepository<KeycloakUser,Long> {

}
