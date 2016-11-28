package com.aliens.msg.mmq.receiver;


import com.aliens.msg.models.Clients;
import com.aliens.msg.config.RabbitMqConfig;
import com.aliens.msg.mmq.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.QueueingConsumer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;


@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@Data
public abstract class MessageReceiver  {


    private static ObjectMapper mapper = new ObjectMapper();

    @Autowired
    RabbitMqConfig rabbitMqConfig;

    @Autowired
    ConnectionFactoryProxy connectionFactory;

    Connection connection;
    Channel channel;


    String threadName;
    String queueName;
    boolean autoAck=false;
    Clients client;

    long startTime= System.currentTimeMillis();


    public MessageReceiver withThreadName(String threadName) {
        this.threadName=threadName;
        return this;
    }

    public MessageReceiver withClient(Clients client) {
        this.client=client;
        //this.queueName=client.getTopic();
        //this.mainQueueName=client.getTopic();
        return this;
    }


    public MessageReceiver withQueueName(String queueName) {
        this.queueName=queueName;
        return this;
    }




    public abstract Status action(Message message) throws Exception;



    public ChannelResponse consumeMessages()  {



        try {
            connection = connectionFactory.getConnection();
            channel = connection.createChannel();
            String mainQueueName = client.getTopic();
            if(!queueName.equals(mainQueueName))
                channel.basicQos(1);

            channel.queueDeclare(queueName, true, false, false, null);
            log.info("listening to queue {}", queueName);

            QueueingConsumer consumer = new QueueingConsumer(channel);

            channel.basicConsume(queueName, autoAck, consumer);

            long timeout =rabbitMqConfig.getTimeout();
            long threadLifeTime =rabbitMqConfig.getThreadLifeTime();

            while (true) {
                QueueingConsumer.Delivery delivery = consumer.nextDelivery(timeout);

                if(delivery==null)
                {

                    if(!queueName.equals(mainQueueName)) {
                        log.info("No message since {} seconds, deleting queue {}", timeout / 1000, queueName);
                        channel.queueDelete(queueName, false, true);
                    }
                    return ChannelResponse.QUEUE_PROCESSED;
                }

                String messageStr = new String(delivery.getBody());


                Message message = mapper.readValue(messageStr.getBytes(), Message.class);
                log.info("Received message {}",message.getMessageId());
                Status status=null;
                try {
                    status=action(message);
                }
                catch (Exception e)
                {
                    return ChannelResponse.MESSAGE_FAILED;
                }

                if(status==Status.RESTART)
                    return ChannelResponse.RESTART;


                if(!autoAck && status==Status.SUCCESS)
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(),false);

                if(queueName.equals(mainQueueName) && System.currentTimeMillis()-startTime > threadLifeTime)
                {
                    return ChannelResponse.SCHEDULED_RESTART;
                }
            }

        }
        catch(Exception e)
        {
            log.info("got error while reading queue {} {}",queueName, ExceptionUtils.getStackTrace(e));
            return ChannelResponse.ERROR;
        }
        finally {
            MMQUtil.ensureClosure(connection,channel);
        }
    }
}
