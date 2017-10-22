package com.training.spring.cloud;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
public class AccountRest {
    @Autowired
    private RemoteEventPublisher eventPublisher;

    @Autowired
    private AccountDAO dao;

    @Autowired
    private AccountRepository repo;

    @PreAuthorize("hasAuthority('ACCOUNT_WRITE')")
    @RequestMapping("/create")
    public void create(@RequestParam("client_id") Integer clientId) {
        dao.create(clientId);
    }

    @PreAuthorize("hasAuthority('ACCOUNT_PROCESS')")
    @RequestMapping("/fund/{id}")
    public boolean fund(@PathVariable Integer id, @RequestParam BigDecimal sum) {
        try {
            return dao.addBalance(id, sum.abs());
        } finally {
            eventPublisher.publishEvent(new FundEvent("AccountService", "HistoryService", sum));
        }
    }

    @PreAuthorize("hasAuthority('ACCOUNT_PROCESS')")
    @RequestMapping("/checkout/{id}")
    public boolean checkout(@PathVariable Integer id, @RequestParam BigDecimal sum) {
        BigDecimal negativeSum = sum.abs().negate();
        try {
            return dao.addBalance(id, negativeSum);
        } finally {
            eventPublisher.publishEvent(new WithdrawEvent("AccountService", "HistoryService", negativeSum));
        }
    }

    @PreAuthorize("hasAuthority('ACCOUNT_READ')")
    @RequestMapping("/get/{clientId}")
    public List<? extends Account> getByClient(@PathVariable Integer clientId) {
        return repo.findByClientId(clientId);
    }
}
