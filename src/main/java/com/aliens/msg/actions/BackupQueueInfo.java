package com.aliens.msg.actions;

import com.aliens.msg.Constants;
import com.aliens.msg.hazelcast.QueueInfo;
import com.aliens.msg.models.HzBackup;
import com.aliens.msg.repositories.HzBackupRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.Wither;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by jayant on 3/10/16.
 */
@Component
@Slf4j
@Scope("prototype")
@NoArgsConstructor
@AllArgsConstructor
public class BackupQueueInfo implements Runnable {

    @Autowired
    HzBackupRepository hzBackupRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Wither
    QueueInfo queueInfo;
    @Wither
    String command;


    public void save()
    {
        HzBackup hzBackup =objectMapper.convertValue(queueInfo,HzBackup.class);
        hzBackupRepository.save(hzBackup);
    }

    public void delete()
    {

        hzBackupRepository.delete(queueInfo.getGroupName());
    }

    @Override
    public void run() {

        switch (command){
            case Constants.SAVE_QUEUE : save();break;
            case Constants.DELETE_QUEUE: delete();break;
        }
    }
}
