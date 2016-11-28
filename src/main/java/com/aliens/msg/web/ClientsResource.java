package com.aliens.msg.web;

import com.codahale.metrics.annotation.Timed;
import com.aliens.msg.models.Clients;

import com.aliens.msg.repositories.ClientsRepository;
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
 * REST controller for managing Clients.
 */
@RestController
@RequestMapping("/api")
public class ClientsResource {

    private final Logger log = LoggerFactory.getLogger(ClientsResource.class);

    @Inject
    private ClientsRepository clientsRepository;

    /**
     * POST  /clients : Create a new clients.
     *
     * @param clients the clients to create
     * @return the ResponseEntity with status 201 (Created) and with body the new clients, or with status 400 (Bad Request) if the clients has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/clients",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Clients> createClients(@RequestBody Clients clients) throws URISyntaxException {
        log.debug("REST request to save Clients : {}", clients);
        if (clients.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("clients", "idexists", "A new clients cannot already have an ID")).body(null);
        }
        Clients result = clientsRepository.save(clients);
        return ResponseEntity.created(new URI("/api/clients/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("clients", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /clients : Updates an existing clients.
     *
     * @param clients the clients to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated clients,
     * or with status 400 (Bad Request) if the clients is not valid,
     * or with status 500 (Internal Server Error) if the clients couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/clients",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Clients> updateClients(@RequestBody Clients clients) throws URISyntaxException {
        log.debug("REST request to update Clients : {}", clients);
        if (clients.getId() == null) {
            return createClients(clients);
        }
        Clients result = clientsRepository.save(clients);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("clients", clients.getId().toString()))
            .body(result);
    }

    /**
     * GET  /clients : get all the clients.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of clients in body
     */
    @RequestMapping(value = "/clients",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Clients> getAllClients() {
        log.debug("REST request to get all Clients");
        List<Clients> clients = clientsRepository.findAll();
        return clients;
    }

    /**
     * GET  /clients/:id : get the "id" clients.
     *
     * @param id the id of the clients to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the clients, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/clients/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Clients> getClients(@PathVariable Long id) {
        log.debug("REST request to get Clients : {}", id);
        Clients clients = clientsRepository.findOne(id);
        return Optional.ofNullable(clients)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /clients/:id : delete the "id" clients.
     *
     * @param id the id of the clients to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/clients/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteClients(@PathVariable Long id) {
        log.debug("REST request to delete Clients : {}", id);
        clientsRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("clients", id.toString())).build();
    }

}
