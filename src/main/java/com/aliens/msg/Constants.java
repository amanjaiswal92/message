package com.aliens.msg;

import lombok.experimental.UtilityClass;
import org.joda.time.DateTimeZone;

/**
 * Created by jayant on 26/9/16.
 */

@UtilityClass
public class Constants {
    public final static String CLUSTERNAME = "clusterMsg";

    public final static String MAIN_QUEUE_WORKER_LIST = "mainQueueWorkerThreads";
    public final static String GROUP_QUEUE_WORKER_LIST = "groupQueueWorkerThreads";

    public final static String RESTARTED_WORKER_LIST = "restartedThreads";
    public final static String QUEUE_MAPPINGS="data";
    public final static String WORKER_STATUS ="worker_info";


    public final static String AVAILABLE_GROUP_SUFFIX = "_AvailableGroups";
    public final static String WAITING_GROUP_SUFFIX = "_WaitingGroups";
    public final static String CLIENTS= "clients";

    public final static String WORKER_ACTIVE ="active";
    public final static String SAVE_QUEUE="SAVE";
    public final static String DELETE_QUEUE="DELETE";

    public final static String DUMMY_USER ="dummy";

    public static final DateTimeZone timeZone = DateTimeZone.forID("Asia/Kolkata");

}
