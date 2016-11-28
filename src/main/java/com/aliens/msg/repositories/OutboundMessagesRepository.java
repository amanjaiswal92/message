package com.aliens.msg.repositories;

import com.aliens.msg.models.OutboundMessages;

import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the InboundMessages entity.
 */
@SuppressWarnings("unused")
public interface OutboundMessagesRepository extends JpaRepository<OutboundMessages,Long> {

}
