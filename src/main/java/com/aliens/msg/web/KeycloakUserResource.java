package com.aliens.msg.web;

import com.codahale.metrics.annotation.Timed;
import com.aliens.msg.models.KeycloakUser;

import com.aliens.msg.repositories.KeycloakUserRepository;
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
 * REST controller for managing KeycloakUser.
 */
@RestController
@RequestMapping("/api")
public class KeycloakUserResource {

    private final Logger log = LoggerFactory.getLogger(KeycloakUserResource.class);

    @Inject
    private KeycloakUserRepository keycloakUserRepository;

    /**
     * POST  /keycloak-users : Create a new keycloakUser.
     *
     * @param keycloakUser the keycloakUser to create
     * @return the ResponseEntity with status 201 (Created) and with body the new keycloakUser, or with status 400 (Bad Request) if the keycloakUser has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/keycloak-users",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<KeycloakUser> createKeycloakUser(@RequestBody KeycloakUser keycloakUser) throws URISyntaxException {
        log.debug("REST request to save KeycloakUser : {}", keycloakUser);
        if (keycloakUser.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("keycloakUser", "idexists", "A new keycloakUser cannot already have an ID")).body(null);
        }
        KeycloakUser result = keycloakUserRepository.save(keycloakUser);
        return ResponseEntity.created(new URI("/api/keycloak-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("keycloakUser", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /keycloak-users : Updates an existing keycloakUser.
     *
     * @param keycloakUser the keycloakUser to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated keycloakUser,
     * or with status 400 (Bad Request) if the keycloakUser is not valid,
     * or with status 500 (Internal Server Error) if the keycloakUser couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/keycloak-users",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<KeycloakUser> updateKeycloakUser(@RequestBody KeycloakUser keycloakUser) throws URISyntaxException {
        log.debug("REST request to update KeycloakUser : {}", keycloakUser);
        if (keycloakUser.getId() == null) {
            return createKeycloakUser(keycloakUser);
        }
        KeycloakUser result = keycloakUserRepository.save(keycloakUser);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("keycloakUser", keycloakUser.getId().toString()))
            .body(result);
    }

    /**
     * GET  /keycloak-users : get all the keycloakUsers.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of keycloakUsers in body
     */
    @RequestMapping(value = "/keycloak-users",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<KeycloakUser> getAllKeycloakUsers() {
        log.debug("REST request to get all KeycloakUsers");
        List<KeycloakUser> keycloakUsers = keycloakUserRepository.findAll();
        return keycloakUsers;
    }

    /**
     * GET  /keycloak-users/:id : get the "id" keycloakUser.
     *
     * @param id the id of the keycloakUser to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the keycloakUser, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/keycloak-users/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<KeycloakUser> getKeycloakUser(@PathVariable Long id) {
        log.debug("REST request to get KeycloakUser : {}", id);
        KeycloakUser keycloakUser = keycloakUserRepository.findOne(id);
        return Optional.ofNullable(keycloakUser)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /keycloak-users/:id : delete the "id" keycloakUser.
     *
     * @param id the id of the keycloakUser to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/keycloak-users/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteKeycloakUser(@PathVariable Long id) {
        log.debug("REST request to delete KeycloakUser : {}", id);
        keycloakUserRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("keycloakUser", id.toString())).build();
    }

}
