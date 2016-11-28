package com.aliens.msg.mmq.receiver;

import com.aliens.msg.actions.BackupQueueInfoProxy;
import com.aliens.msg.hazelcast.MsgCache;
import com.aliens.msg.hazelcast.QueueInfo;
import com.aliens.msg.hazelcast.QueueState;
import com.aliens.msg.mmq.Message;
import com.aliens.msg.mmq.Status;
import com.aliens.msg.mmq.actions.MessageSender;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

import static com.aliens.msg.Constants.timeZone;

/**
 * Created by jayant on 25/9/16.
 */
@Component
@Scope("prototype")
@Slf4j
public class MainQueueMessageReceiver extends MessageReceiver {

    @Autowired
    MsgCache msgCache;
    @Autowired
    BackupQueueInfoProxy backupQueueInfoProxy;

    @Autowired
    MessageSender messageSender;

    @Override
    public Status action(Message message) throws Exception {

        String groupId=message.getGroupId();
        String clientName=client.getName();

        if(Strings.isNullOrEmpty(groupId))
            return Status.FAILED;

        Optional<QueueInfo> queueInfoOptional= msgCache.findByGroupId(groupId);

        if(queueInfoOptional.isPresent())
        {
            String qname=queueInfoOptional.get().getQueueName();
            messageSender.sendMessage(message,qname);
            return Status.SUCCESS;
        }
        else
        {
            int size= msgCache.getSize();
            if(size<rabbitMqConfig.getQueueLimit())
            {

                if(msgCache.isWaiting(clientName,groupId))
                {
                    return Status.WAITING;
                }

                String queueName = UUID.randomUUID().toString();

                messageSender.sendMessage(message,queueName);

                QueueInfo queueInfo=QueueInfo.builder()
                    .queueName(queueName)
                    .createdAt(LocalDateTime.now(timeZone).toString())
                    .clientName(clientName)
                    .groupName(groupId)
                    .build();

                msgCache.updateData(queueInfo, QueueState.IDLE);
                msgCache.updateAvailbleQueue(clientName,groupId);
                backupQueueInfoProxy.save(queueInfo);
                return Status.SUCCESS;
            }
            else
            {
                msgCache.putToWait(clientName,groupId);
                log.info("queues limit crossed: {} . waiting...",rabbitMqConfig.getQueueLimit());
                return Status.WAITING;
            }
        }

    }


}
