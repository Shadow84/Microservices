package com.training.spring.cloud;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * Created by Privat on 22.10.2017.
 */
public interface SubscriberChannel {
    String UPPER_WORDS = "upperWords";

    @Input(UPPER_WORDS)
    SubscribableChannel upperWords();
}
