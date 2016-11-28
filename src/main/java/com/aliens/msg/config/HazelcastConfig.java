package com.aliens.msg.config;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.inject.Singleton;

/**
 * Created by jayant on 5/10/16.
 */

@Data
@EqualsAndHashCode(callSuper = false)
@Component
@Singleton
@Configuration
@ConfigurationProperties(prefix = "spring.hazelcast")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HazelcastConfig {
    String accessKey;
    String secretKey;
    String regionCode;
    String tagKey;
    String tagValue;
    boolean aws;
    String instanceName;
    String awsUrl;
    int port;
    String hostHeader;
    String securityGroup;
}
