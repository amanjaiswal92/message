package com.aliens.msg.models;

import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;

/**
 * A Clients.
 */
@Entity
@Table(name = "clients")
@Data
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Clients implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "topic")
    private String topic;

    @Column(name = "consumer_count")
    private Integer consumerCount;

    @Column(name = "target_endpoint")
    private String targetEndpoint;

    @Enumerated(EnumType.STRING)
    private ConsumerType consumerType=ConsumerType.SINGLE;


    int bulkCount;

    @Enumerated(EnumType.STRING)
    ClientStatus status=ClientStatus.ACTIVE;

    @ManyToOne(fetch =FetchType.EAGER )
    @JoinColumn(name="keycloak_user")
    private KeycloakUser keycloakUser;


    private boolean groupingEnabled;

}
