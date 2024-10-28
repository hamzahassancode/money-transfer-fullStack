package com.progress.induction.cliqtransfers.service;

import com.progress.induction.cliqtransfers.exception.NotFoundException;
import com.progress.induction.cliqtransfers.model.Account;
import com.progress.induction.cliqtransfers.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountsService {

    private final AccountRepository accountRepository;


    public List<Account> listAccounts() {
        List<Account> accountList = accountRepository.findAll();
        log.info("---Accounts Retrieved from DB---");
        log.info("--- {} ---", accountList);
        return accountList;

    }

    public Account getAccountByAccountNumber(String accountNumber) {
        Account account = accountRepository.findById(accountNumber).orElseThrow(() -> new NotFoundException("no account found"));
        log.info("---Account Retrieved from DB---");
        log.info("--- {} ---", account);
        return account;
    }

}