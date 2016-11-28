package com.aliens.msg.mmq.worker;

import com.aliens.msg.models.Clients;
import com.aliens.msg.hazelcast.MsgCache;
import com.aliens.msg.Constants;
import com.aliens.msg.mmq.ChannelResponse;
import com.aliens.msg.mmq.receiver.MainQueueMessageReceiver;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Wither;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import javax.inject.Provider;
import java.util.UUID;

/**
 * Created by jayant on 25/9/16.
 */
@Slf4j
@Component
@Scope("prototype")
@AllArgsConstructor
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MainQueueWorker implements Runnable {


    final MsgCache msgCache;
    final Provider<MainQueueMessageReceiver> mainQueueMessageReceiverProvider;

    @Wither
    Clients client;

    String threadName;

    @Override
    public void run() {

        threadName = client.getName()+"_"+UUID.randomUUID().toString();
        Thread.currentThread().setName(threadName);

        msgCache.addToSet(Constants.MAIN_QUEUE_WORKER_LIST, threadName);

        while (true) {
            ChannelResponse response = mainQueueMessageReceiverProvider
                .get()
                .withThreadName(threadName)
                .withClient(client)
                .withQueueName(client.getTopic())
                .consumeMessages();

            log.info("MainQueue consumer response: {}",response);

            switch (response) {
                case QUEUE_PROCESSED:
                case SCHEDULED_RESTART:
                case RESTART:
                    log.info("Restarting mainQueue consumer thread");
                    msgCache.clearWaitingList(client.getName());
                    break;
                case ERROR:
                case MESSAGE_FAILED:
                    log.error("Got error while processing main queue");
            }
        }

    }

    @PreDestroy
    public void destroy()
    {
        msgCache.removeFromSet(Constants.MAIN_QUEUE_WORKER_LIST, threadName);
    }
}
