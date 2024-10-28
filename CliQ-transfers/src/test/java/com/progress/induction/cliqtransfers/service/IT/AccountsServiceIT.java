package com.progress.induction.cliqtransfers.service.IT;

import com.progress.induction.cliqtransfers.model.Account;
import com.progress.induction.cliqtransfers.repository.AccountRepository;
import com.progress.induction.cliqtransfers.service.AccountsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class AccountsServiceIT {

    @Autowired
    private AccountsService accountsService;

    @Autowired
    private AccountRepository accountRepository;


    @Test
    void givenAccountsInDatabase_whenListingAccounts_thenReturnAllAccounts() {
        Account account1 = new Account("1234567890123456", new BigDecimal("500.0"));
        Account account2 = new Account("6543210987654321", new BigDecimal("1000.0"));
        List<Account> expectedAccounts = List.of(account1, account2);
        accountRepository.save(account1);
        accountRepository.save(account2);

        List<Account> accounts = accountsService.listAccounts();

        assertEquals(2, accounts.size());
        assertEquals(expectedAccounts, accounts);
    }


    @Test
    void givenAccount_whenGetAccountByAccountNumber_thenReturnAccount() {
        Account account1 = new Account("1234567890123456", new BigDecimal("500.0"));
        Account account2 = new Account("6543210987654321", new BigDecimal("1000.0"));
        accountRepository.save(account1);
        accountRepository.save(account2);

        Account account = accountsService.getAccountByAccountNumber("6543210987654321");

        assertEquals(account2, account);
    }

}
