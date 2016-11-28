package com.aliens.msg.hazelcast;

import com.aliens.msg.mmq.ChannelResponse;

import java.util.Optional;

/**
 * Created by jayant on 25/9/16.
 */
public interface MsgCache<T>  {


    void updateAvailbleQueue(String clientName, String str);

    Optional<QueueInfo> findIdleQueue(String clientName);

    HzStat getStat();

    boolean isWorkerRequired(String clientName);

    void updateWorkerStatus(String clientName,String status);

    void addToSet(String setName, String ele);

    void removeFromSet(String setName, String ele);

    void putToWait(String clientName, String ele);

    boolean isWaiting(String clientName, String groupId);

    void clearWaitingList(String clientName);

    Optional<T> findByGroupId(String groupId);

    int getSize();

    void deleteKey(String key);

    void updateData(T queueInfo, ChannelResponse response);

    void updateData(T queueInfo, QueueState queueState);

    void resetQueue(String groupId, QueueState queueState);

}
