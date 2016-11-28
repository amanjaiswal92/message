package com.aliens.msg.models;

import lombok.Getter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;


@Entity
@Table(name = "outbound_messages")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class OutboundMessages implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final long MAX_PAYLOAD_SIZE =15000;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "message_id")
    private String messageId;

    @Column(name = "group_id")
    private String groupId;

    @Getter
    private String payload;

    public void setPayload(String payload)
    {
        if(payload.length()< MAX_PAYLOAD_SIZE)
            this.payload = payload;
        else this.payload="Payload very big";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessageId() {
        return messageId;
    }

    public OutboundMessages messageId(String messageId) {
        this.messageId = messageId;
        return this;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getGroupId() {
        return groupId;
    }

    public OutboundMessages groupId(String groupId) {
        this.groupId = groupId;
        return this;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OutboundMessages outboundMessages = (OutboundMessages) o;
        if(outboundMessages.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, outboundMessages.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "InboundMessages{" +
            "id=" + id +
            ", messageId='" + messageId + "'" +
            ", groupId='" + groupId + "'" +
            '}';
    }
}
