package com.aliens.msg.hazelcast;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.LocalDateTime;

import java.io.Serializable;

import static com.aliens.msg.Constants.timeZone;

/**
 * Created by jayant on 21/9/16.
 */

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class QueueInfo implements Serializable {


    String createdAt = LocalDateTime.now(timeZone).toString();

    String queueName;
    String clientName;
    String groupName;
    String threadName;
    QueueState state = QueueState.IDLE;
    int retry = 0;


    public void incRetry(int val) {
        retry += val;
    }
}
