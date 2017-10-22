package com.training.spring.cloud;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface TestChannel {
    String CHANNEL_NAME = "testChannel";

    @Output(CHANNEL_NAME)
    MessageChannel output();
}