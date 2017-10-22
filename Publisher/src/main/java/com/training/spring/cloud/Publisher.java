package com.training.spring.cloud;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableBinding(TestChannel.class)
@RestController
public class Publisher {
    @Autowired
    @Qualifier(PublisherChannel.WORDS)
    public MessageChannel output;

    @GetMapping("/publish")
    private void publish(@RequestParam String word) {
        output.send(MessageBuilder.withPayload(word).build());
    }

    public static void main(String[] args) {
        SpringApplication.run(Publisher.class, args);
    }
}