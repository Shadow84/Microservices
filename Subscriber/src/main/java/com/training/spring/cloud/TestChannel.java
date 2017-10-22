package com.training.spring.cloud;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface TestChannel {
    String CHANNEL_NAME = "testChannel";

    @Input(CHANNEL_NAME)
    SubscribableChannel input();
}
