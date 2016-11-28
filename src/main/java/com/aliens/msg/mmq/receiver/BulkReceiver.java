package com.aliens.msg.mmq.receiver;

import com.aliens.msg.mmq.Message;
import com.aliens.msg.mmq.Status;
import com.aliens.msg.mmq.actions.SendMessageToClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.inject.Provider;
import java.util.List;

/**
 * Created by jayant on 29/9/16.
 */

@Component
@Scope("prototype")
public class BulkReceiver extends BaseBulkMessageReceiver {



    @Autowired
    Provider<SendMessageToClient> sendMessageToClientProvider;

    @Override
    public Status action(List<Message> messageList) throws Exception {

        sendMessageToClientProvider.get()
            .withClient(client)
            .invoke(messageList);

        return Status.SUCCESS;
    }
}
