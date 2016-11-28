package com.aliens.msg.repositories;

import com.aliens.msg.models.Clients;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Clients entity.
 */
@SuppressWarnings("unused")
public interface ClientsRepository extends JpaRepository<Clients,Long> {

}
