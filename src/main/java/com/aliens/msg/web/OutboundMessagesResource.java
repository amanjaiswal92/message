package com.aliens.msg.web;

import com.codahale.metrics.annotation.Timed;
import com.aliens.msg.models.OutboundMessages;

import com.aliens.msg.repositories.OutboundMessagesRepository;
import com.aliens.hipster.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing InboundMessages.
 */
@RestController
@RequestMapping("/api")
public class OutboundMessagesResource {

    private final Logger log = LoggerFactory.getLogger(OutboundMessagesResource.class);

    @Inject
    private OutboundMessagesRepository outboundMessagesRepository;

    /**
     * POST  /inbound-messages : Create a new inboundMessages.
     *
     * @param outboundMessages the inboundMessages to create
     * @return the ResponseEntity with status 201 (Created) and with body the new inboundMessages, or with status 400 (Bad Request) if the inboundMessages has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/inbound-messages",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<OutboundMessages> createInboundMessages(@RequestBody OutboundMessages outboundMessages) throws URISyntaxException {
        log.debug("REST request to save InboundMessages : {}", outboundMessages);
        if (outboundMessages.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("inboundMessages", "idexists", "A new inboundMessages cannot already have an ID")).body(null);
        }
        OutboundMessages result = outboundMessagesRepository.save(outboundMessages);
        return ResponseEntity.created(new URI("/api/inbound-messages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("inboundMessages", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /inbound-messages : Updates an existing inboundMessages.
     *
     * @param outboundMessages the inboundMessages to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated inboundMessages,
     * or with status 400 (Bad Request) if the inboundMessages is not valid,
     * or with status 500 (Internal Server Error) if the inboundMessages couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/inbound-messages",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<OutboundMessages> updateInboundMessages(@RequestBody OutboundMessages outboundMessages) throws URISyntaxException {
        log.debug("REST request to update InboundMessages : {}", outboundMessages);
        if (outboundMessages.getId() == null) {
            return createInboundMessages(outboundMessages);
        }
        OutboundMessages result = outboundMessagesRepository.save(outboundMessages);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("inboundMessages", outboundMessages.getId().toString()))
            .body(result);
    }

    /**
     * GET  /inbound-messages : get all the inboundMessages.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of inboundMessages in body
     */
    @RequestMapping(value = "/inbound-messages",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<OutboundMessages> getAllInboundMessages() {
        log.debug("REST request to get all InboundMessages");
        List<OutboundMessages> inboundMessages = outboundMessagesRepository.findAll();
        return inboundMessages;
    }

    /**
     * GET  /inbound-messages/:id : get the "id" inboundMessages.
     *
     * @param id the id of the inboundMessages to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the inboundMessages, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/inbound-messages/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<OutboundMessages> getInboundMessages(@PathVariable Long id) {
        log.debug("REST request to get InboundMessages : {}", id);
        OutboundMessages outboundMessages = outboundMessagesRepository.findOne(id);
        return Optional.ofNullable(outboundMessages)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /inbound-messages/:id : delete the "id" inboundMessages.
     *
     * @param id the id of the inboundMessages to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/inbound-messages/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteInboundMessages(@PathVariable Long id) {
        log.debug("REST request to delete InboundMessages : {}", id);
        outboundMessagesRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("inboundMessages", id.toString())).build();
    }

}
