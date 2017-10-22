package com.training.spring.cloud;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CardRest {
    @Autowired
    CardNumberGenerator cardNumberGenerator;

    @RequestMapping("create")
    @PreAuthorize("hasAuthority('CARD_WRITE')")
    public String createNewCard() {
        return cardNumberGenerator.generate();
    }
}
