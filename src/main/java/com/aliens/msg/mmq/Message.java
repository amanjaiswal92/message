package com.aliens.msg.mmq;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.joda.time.DateTimeZone;

import java.io.Serializable;

/**
 * Created by jayant on 15/9/16.
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Message implements Serializable {

    protected static final DateTimeZone timeZone = DateTimeZone.forID("Asia/Kolkata");

    String payload;
    String createdAt;
    String messageId;
    String groupId;
}
