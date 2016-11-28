package com.aliens.msg.mmq.actions;

import com.aliens.msg.mmq.ConnectionFactoryProxy;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.Wither;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by jayant on 4/10/16.
 */
@Component
@Scope("prototype")
@AllArgsConstructor
@NoArgsConstructor
public class   DeleteQueue {

    @Autowired
    ConnectionFactoryProxy connectionFactory;

    @Wither
    String queueName;

    public void invoke() throws Exception {
        Connection connection = connectionFactory.getConnection();
        Channel channel = null;
        try {
            channel = connection.createChannel();
            channel.queueDelete(queueName, false, false);
        } catch (Exception e) {
            throw e;
        } finally {
            if (channel != null && channel.isOpen())
                channel.close();
        }
    }
}
