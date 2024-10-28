package com.progress.induction.cliqtransfers.controller;

import com.progress.induction.cliqtransfers.model.Account;
import com.progress.induction.cliqtransfers.service.AccountsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v1/accounts")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class AccountsController {
    private final AccountsService accountsService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Account> getAccounts() {
        log.info("--- Start get Accounts controller---");
        return accountsService.listAccounts();
    }

    @GetMapping("/{account_number}")
    @ResponseStatus(HttpStatus.OK)
    public Account getAccountByAccountNumber(@PathVariable String account_number) {
        log.info("--- Start get Account controller---");
        return accountsService.getAccountByAccountNumber(account_number);
    }

}
