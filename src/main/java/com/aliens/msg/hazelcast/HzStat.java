package com.aliens.msg.hazelcast;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * Created by jayant on 25/9/16.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HzStat {

    Set<String> clients;
    Set<String> mainQueueworkerThreads;
    Set<String> groupQueueWorkerThreads;
    Set<String> restartedThreads;

    Map<String,Set<String>> waitingGroups = new HashMap<>();
    Map<String,Queue<String>> queue = new HashMap<>();


    Map<String, String> workerStatus;
    Map<String, QueueInfo> mappings;


}
