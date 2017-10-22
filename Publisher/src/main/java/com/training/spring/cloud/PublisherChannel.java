package com.training.spring.cloud;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * Created by Privat on 22.10.2017.
 */
public interface PublisherChannel {
    String WORDS = "words";

    @Output(WORDS)
    MessageChannel words();
}
