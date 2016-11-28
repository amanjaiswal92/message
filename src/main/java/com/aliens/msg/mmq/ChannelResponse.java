package com.aliens.msg.mmq;

import lombok.Getter;

/**
 * Created by jayant on 21/9/16.
 */
public enum  ChannelResponse {
    QUEUE_PROCESSED("QUEUE_PROCESSED")
    ,MESSAGE_FAILED("MESSAGE_FAILED")
    ,ERROR("ERROR"),RESTART("RESTART"),SCHEDULED_RESTART("SCHEDULED_RESTART")
    ,NOT_ENOUGH_MESSAGES("NOT_ENOUGH_MESSAGES");

    @Getter
    String value;

    ChannelResponse(String value)
    {
        this.value =value;
    }
}
