package com.training.spring.cloud;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@PreAuthorize("hasAuthority('PROCESSING')")
public class ProcessingRest {
    @Autowired
    ProcessingRepository processingRepository;

    @Autowired
    AccountServiceClient accountServiceClient;

    @Autowired
    CardServiceClient cardServiceClient;

    @RequestMapping("/issue/{accountId}")
    public String issueNewCard(@PathVariable Integer accountId) {
        final String card = cardServiceClient.createCard();
        if (card == null) {
            return "CARD_SERVICE_NOT_AVAILABLE";
        }
        ProcessingEntity pe = new ProcessingEntity();
        pe.setCard(card);
        pe.setAccountId(accountId);
        processingRepository.save(pe);
        return card;
    }

    @RequestMapping("/checkout/{card}")
    public boolean checkout(@PathVariable String card, @RequestParam BigDecimal sum) {
        ProcessingEntity pe = processingRepository.findByCard(card);
        if (pe == null) {
            return false;
        }
        return accountServiceClient.checkout(pe.getAccountId(), sum);
    }

    @RequestMapping("/get")
    public Map<Integer, String> getByAccount(@RequestParam("account_id") List<Integer> accountIdList) {
        return processingRepository.findByAccountIdIn(accountIdList)
                .stream()
                .collect(Collectors.toMap(ProcessingEntity::getAccountId, ProcessingEntity::getCard));
    }

    @HystrixCommand(fallbackMethod = "testFallback")
    @RequestMapping("/test")
    public String testHystrix(Boolean fail) {
        if (Boolean.TRUE.equals(fail)) {
            throw new RuntimeException();
        }
        return "OK";
    }

    private String testFallback(Boolean fail) {
        return "FAILED";
    }
}
