package com.aliens.msg.config;

import com.aliens.msg.keycloak.UserCredentials;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.inject.Singleton;
import java.util.HashMap;

@Data
@EqualsAndHashCode(callSuper = false)
@Component
@Singleton
@ConfigurationProperties(prefix = "spring.keycloakCreds")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KeyCloakConfig {

	String Authorization;
	String keycloakGetAccessTokenUrl;
    long  cacheDuration;

	HashMap<String, UserCredentials> map=new HashMap<>();

}
