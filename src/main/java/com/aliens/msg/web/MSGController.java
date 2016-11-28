package com.aliens.msg.web;

import com.aliens.msg.actions.DeleteGroupAction;
import com.aliens.msg.hazelcast.MsgCache;
import com.aliens.msg.hazelcast.HzStat;
import com.aliens.msg.hazelcast.QueueState;
import com.aliens.msg.mmq.ThreadWrapper;
import com.aliens.msg.mmq.actions.VerifyGrouping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * Created by jayant on 25/9/16.
 */

@RestController
@RequestMapping("/msg")
public class MSGController {

    @Autowired
    MsgCache msgCache;

    @Autowired
    ThreadWrapper threadWrapper;

    @Autowired
    VerifyGrouping verifyGrouping;

    @Autowired
    DeleteGroupAction deleteGroupAction;



    @RequestMapping(value = "",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> stat() throws Exception {

        HzStat hzStat = msgCache.getStat();
        return ResponseEntity.ok().body(hzStat);
    }






    @RequestMapping(value = "/test/verify",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> verify() throws Exception {

        Set<String> result=verifyGrouping.invoke();
        return ResponseEntity.ok().body(result);
    }

    @RequestMapping(value = "/testresponse",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<?> testResponse() throws Exception {

            String result = "true";
            return ResponseEntity.ok().body(result);
        }

    @RequestMapping(value = "/test/start",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> startTest() throws Exception {

        threadWrapper.sendTestMessages();
        return ResponseEntity.ok().body("start");
    }



    @RequestMapping(value = "/reset/{groupId}",
            method = RequestMethod.POST,
            produces = MediaType.TEXT_PLAIN_VALUE)
        public ResponseEntity<?> resetQueue(@RequestBody Object req,@PathVariable("groupId") String groupId) throws Exception {
    	msgCache.resetQueue(groupId, QueueState.IDLE);
    	return ResponseEntity.ok().body("Queue is reset");
        }

    @RequestMapping(value = "/delete/{groupId}",
        method = RequestMethod.POST,
        produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<?> deleteQueue(@RequestBody Object req,@PathVariable("groupId") String groupId) throws Exception {
        deleteGroupAction.withGroupId(groupId).invoke();
        return ResponseEntity.ok().body("Queue is deleted");
    }



}
