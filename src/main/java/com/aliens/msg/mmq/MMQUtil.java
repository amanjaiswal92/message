package com.aliens.msg.mmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 * Created by jayant on 27/9/16.
 */
@Slf4j
public class MMQUtil {

    public static void ensureClosure(Connection connection, Channel channel)
    {
        try {

            if (channel != null && channel.isOpen())
                channel.close();
        }
        catch (Exception e)
        {
            log.error("Unable to close channel {}", ExceptionUtils.getStackTrace(e));
        }
    }
}
