package com.aliens.msg.init;

import com.aliens.msg.repositories.KeycloakUserRepository;
import com.aliens.msg.config.KeyCloakConfig;
import com.aliens.msg.keycloak.UserCredentials;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.inject.Singleton;

/**
 * Created by jayant on 1/10/16.
 */
@Component
@Singleton
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LoadKeycloakUsers  implements BootStrap {

   final KeycloakUserRepository keycloakUserRepository;
   final KeyCloakConfig keyCloakConfig;
   final ObjectMapper objectMapper;

    @Override
   public void setup()
   {
       keycloakUserRepository.findAll().forEach( keycloakUser -> {

           UserCredentials userCredentials= objectMapper.convertValue(keycloakUser, UserCredentials.class);
           keyCloakConfig.getMap().put(keycloakUser.getName(),userCredentials);
       });

       log.info(keyCloakConfig.getMap().toString());
   }
}
