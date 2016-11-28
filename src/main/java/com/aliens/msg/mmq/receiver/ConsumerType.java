package com.aliens.msg.mmq.receiver;

/**
 * Created by jayant on 28/9/16.
 */
public enum ConsumerType {
 SINGLE("SINGLE"),BULK("BULK");

   String value;

    ConsumerType(String value)
   {
       this.value=value;
   }
}
