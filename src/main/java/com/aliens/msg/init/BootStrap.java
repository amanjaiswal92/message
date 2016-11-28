package com.aliens.msg.init;

/**
 * Created by jayant on 1/10/16.
 */
@FunctionalInterface
public interface BootStrap {
    public void setup() throws Exception;
}


