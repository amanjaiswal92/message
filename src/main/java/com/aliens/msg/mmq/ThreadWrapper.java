package com.aliens.msg.mmq;

import com.aliens.msg.Constants;
import com.aliens.msg.hazelcast.MsgCache;
import com.aliens.msg.mmq.test.TestMessageSender;
import com.aliens.msg.mmq.worker.GroupQueueWorker;
import com.aliens.msg.mmq.worker.MainQueueWorker;
import com.aliens.msg.models.ClientStatus;
import com.aliens.msg.models.Clients;
import com.aliens.msg.repositories.ClientsRepository;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

/**
 * Created by jayant on 25/9/16.
 */
@Slf4j
@Component
@Singleton
public class ThreadWrapper  {

    @Autowired
    Provider<GroupQueueWorker> groupQueueWorkerProvider;

    @Autowired
    Provider<MainQueueWorker> mainQueueWorkerProvider;

    @Autowired
    Provider<TestMessageSender> testMessageSenderProvider;

    @Autowired
    ClientsRepository clientsRepository;

    @Autowired
    MsgCache msgCache;

    Set<String> workingClients= Sets.newHashSet();
    final int maxClients=4;


    ExecutorService executorService = Executors.newFixedThreadPool(50);


    public void startThreads(Clients client)
    {
        executorService.submit(mainQueueWorkerProvider.get().withClient(client));

        IntStream.range(0, client.getConsumerCount()).forEach((x) ->

            executorService.submit(groupQueueWorkerProvider.get().withClient(client)));

        msgCache.addToSet(Constants.CLIENTS, client.getName());
        workingClients.add(client.getName());
    }

    @Scheduled(fixedDelay = 1000*60)
    public void setup()
    {

    	List<Clients> clients = clientsRepository.findAll();


    	clients.stream().filter(client-> client.getStatus().equals(ClientStatus.ACTIVE))
            .forEach(
                client -> {

                if(msgCache.isWorkerRequired(client.getName()) && workingClients.size()<maxClients) {

                    startThreads(client);
                }

                });

    }

    public void sendTestMessages()
    {
        List<Clients> clients = clientsRepository.findAll();

        clients.stream().filter(client-> client.getStatus().equals(ClientStatus.ACTIVE))
            .forEach(client -> {
                IntStream.range(0,5)
                    .forEach( (x)->executorService.submit(
                        testMessageSenderProvider.get().withQueName(client.getTopic())
                            .withGroupId(client.getName()+"_g"+String.valueOf(x))));
            });
    }

    @Scheduled(fixedDelay = 1000*60)
    public void healthCheck()
    {

        workingClients.forEach(client->
            msgCache.updateWorkerStatus(client,Constants.WORKER_ACTIVE));
    }
}
