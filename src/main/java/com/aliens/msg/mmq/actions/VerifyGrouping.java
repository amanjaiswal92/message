package com.aliens.msg.mmq.actions;

import com.aliens.msg.models.OutboundMessages;
import com.aliens.msg.repositories.OutboundMessagesRepository;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by jayant on 27/9/16.
 */
@Component
@Slf4j
public class VerifyGrouping {

    @Autowired
    OutboundMessagesRepository outboundMessagesRepository;

    public Set<String> invoke()
    {
        boolean health=true;
        Set<String> st= Sets.newConcurrentHashSet();

        Map<String,List<OutboundMessages>> map=
        outboundMessagesRepository
            .findAll().stream()
            .collect(Collectors.groupingBy(OutboundMessages::getGroupId));

         for(List<OutboundMessages> list: map.values()) {
             int i, sz = list.size();
             for (i = 1; i <= sz; i++) {
                 int x=Integer.parseInt(list.get(i - 1).getMessageId());
                 if (x!= i) {
                     st.add(list.get(i-1).getGroupId());
                     break;
                 }
             }
         }

     return st;
    }
}
