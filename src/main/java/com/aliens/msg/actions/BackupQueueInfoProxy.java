package com.aliens.msg.actions;

import com.aliens.msg.Constants;
import com.aliens.msg.hazelcast.QueueInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by jayant on 3/10/16.
 */

@Component
@Singleton
public class BackupQueueInfoProxy {

    @Autowired
    Provider<BackupQueueInfo> backupQueueInfoProvider;

    ExecutorService executorService = Executors.newFixedThreadPool(50);

    public void save(QueueInfo queueInfo)
    {
        executorService.submit(backupQueueInfoProvider.get().withQueueInfo(queueInfo).withCommand(Constants.SAVE_QUEUE));
    }

    public void delete(QueueInfo queueInfo)
    {
        executorService.submit(backupQueueInfoProvider.get().withQueueInfo(queueInfo).withCommand(Constants.DELETE_QUEUE));
    }
}
