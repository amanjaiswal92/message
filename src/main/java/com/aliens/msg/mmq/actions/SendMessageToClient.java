package com.aliens.msg.mmq.actions;

import com.aliens.msg.Constants;
import com.aliens.msg.mmq.Message;
import com.aliens.msg.models.Clients;
import com.aliens.msg.models.OutboundMessages;
import com.aliens.msg.repositories.OutboundMessagesRepository;
import com.aliens.msg.utils.RestUtil;
import com.google.common.base.Strings;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.Wither;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
@NoArgsConstructor
@Scope("prototype")
public class SendMessageToClient {

    @Wither
    Clients client;

    @Autowired
    RestUtil restUtil;

    @Autowired
    OutboundMessagesRepository outboundMessagesRepository;

    private boolean targetSpecified() {
        return !Strings.isNullOrEmpty(client.getTargetEndpoint());
    }

    private void applyKeycloak() {
        if (!client.getKeycloakUser().getName().equals(Constants.DUMMY_USER)) {
            restUtil = restUtil.withKeycloakUser(client.getKeycloakUser());
        }
    }

    private void saveOutBound(Message message) {
        OutboundMessages outboundMessages = new OutboundMessages();
        outboundMessages.setGroupId(message.getGroupId());
        outboundMessages.setMessageId(message.getMessageId());
        outboundMessages.setPayload(message.getPayload());
        outboundMessagesRepository.save(outboundMessages);
    }

    public String invoke(Message message) throws Exception {
        String response = null;
        if (targetSpecified()) {
            applyKeycloak();
            response = restUtil.post(client.getTargetEndpoint(), message, String.class);
        }

        saveOutBound(message);
        return response;
    }

    public String invoke(List<Message> messageList) throws Exception {
        String response = null;
        if (targetSpecified()) {
            applyKeycloak();
            response = restUtil.post(client.getTargetEndpoint(), messageList, String.class);
        }

        messageList.forEach(message -> saveOutBound(message));
        return response;
    }


}
