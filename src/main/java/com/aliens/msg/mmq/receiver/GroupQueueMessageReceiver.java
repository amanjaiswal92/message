package com.aliens.msg.mmq.receiver;

import com.aliens.msg.mmq.Message;
import com.aliens.msg.mmq.Status;
import com.aliens.msg.mmq.actions.SendMessageToClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.inject.Provider;

/**
 * Created by jayant on 25/9/16.
 */
@Component
@Scope("prototype")
public class GroupQueueMessageReceiver extends MessageReceiver {

	@Autowired
    Provider<SendMessageToClient> sendMessageToClientProvider;

    @Override
	public Status action(Message message) throws Exception
    {

        sendMessageToClientProvider.get()
            .withClient(client)
            .invoke(message);

        return Status.SUCCESS;
    }

}
