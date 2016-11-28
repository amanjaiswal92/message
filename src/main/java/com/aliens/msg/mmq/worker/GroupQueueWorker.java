package com.aliens.msg.mmq.worker;

import com.aliens.msg.actions.BackupQueueInfoProxy;
import com.aliens.msg.config.RabbitMqConfig;
import com.aliens.msg.hazelcast.MsgCache;
import com.aliens.msg.Constants;
import com.aliens.msg.hazelcast.QueueInfo;
import com.aliens.msg.hazelcast.QueueState;
import com.aliens.msg.mmq.ChannelResponse;
import com.aliens.msg.mmq.receiver.BulkReceiver;
import com.aliens.msg.mmq.receiver.GroupQueueMessageReceiver;
import com.aliens.msg.models.Clients;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Wither;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import javax.inject.Provider;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by jayant on 25/9/16.
 */

@Slf4j
@Scope("prototype")
@Data
@Component
@AllArgsConstructor
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GroupQueueWorker implements Runnable {


    final MsgCache msgCache;
    final RabbitMqConfig rabbitMqConfig;

    final Provider<GroupQueueMessageReceiver> groupQueueMessageReceiverProvider;
    final Provider<BulkReceiver> bulkReceiverProvider;
    final BackupQueueInfoProxy backupQueueInfo;

    @Wither
    Clients client;

    String threadName;

    public void run()
    {
        threadName= client.getName()+"_"+UUID.randomUUID().toString();
        Thread.currentThread().setName(threadName);

        msgCache.addToSet(Constants.GROUP_QUEUE_WORKER_LIST,threadName);

        while(true)
        {
            Optional<QueueInfo> queueInfoOptional = msgCache.findIdleQueue(client.getName());
            long sleepInterval =rabbitMqConfig.getSleepInterval();

            if(queueInfoOptional.isPresent())
            {
                QueueInfo queueInfo =queueInfoOptional.get();
                String qname=queueInfo.getQueueName();
                queueInfo.setThreadName(threadName);
                msgCache.updateData(queueInfo, QueueState.PROCESSING);

                ChannelResponse response;

                switch (client.getConsumerType()) {

                    case BULK:

                        boolean checkSize = true;

                        LocalDateTime queueCreatedAt = new LocalDateTime(queueInfo.getCreatedAt());

                        if (queueCreatedAt.isBefore(LocalDateTime.now(Constants.timeZone).minusSeconds(rabbitMqConfig.getMaxWaitTime())))
                            checkSize = false;

                        response = bulkReceiverProvider
                            .get()
                            .withParams(threadName, client, qname, checkSize)
                            .consumeMessages();
                        break;

                    case SINGLE:
                     default:

                        response = groupQueueMessageReceiverProvider
                            .get()
                            .withClient(client)
                            .withQueueName(qname)
                            .withThreadName(threadName)
                            .consumeMessages();
                }


                msgCache.updateData(queueInfo,response);

                if(response.equals(ChannelResponse.QUEUE_PROCESSED))
                {
                    backupQueueInfo.delete(queueInfo);
                }
            }
            else {
                log.info("No idle queue found : let me sleep {}",sleepInterval);
                try {
                    Thread.sleep(sleepInterval,0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }

    }

    @PreDestroy
    public void destroy()
    {
        msgCache.removeFromSet(Constants.GROUP_QUEUE_WORKER_LIST, threadName);
    }

}
