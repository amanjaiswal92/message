package com.aliens.msg.init;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

/**
 * Created by jayant on 30/9/16.
 */

@Component
@Slf4j
@Singleton
public class Initialize {

    @Inject
    List<BootStrap> bootStrapList;

    @PostConstruct
    public void setup() throws Exception {

        for(BootStrap instance : bootStrapList)
            instance.setup();

        log.info("Setup done");
    }
}
