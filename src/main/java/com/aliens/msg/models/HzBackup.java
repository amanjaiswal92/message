package com.aliens.msg.models;

import lombok.Data;

import javax.persistence.*;

/**
 * Created by jayant on 3/10/16.
 */

@Entity
@Table(name = "hzbackup")
@Data
public class HzBackup {

    @Id
    @Column(name = "group_name")
    private String groupName;

    @Column(name = "queue_name")
    private String queueName;

    @Column(name = "client_name")
    private String clientName;
}
