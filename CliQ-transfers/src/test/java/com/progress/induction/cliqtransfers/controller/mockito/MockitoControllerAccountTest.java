package com.progress.induction.cliqtransfers.controller.mockito;

import com.progress.induction.cliqtransfers.controller.AccountsController;
import com.progress.induction.cliqtransfers.exception.GlobalExceptionHandler;
import com.progress.induction.cliqtransfers.model.Account;
import com.progress.induction.cliqtransfers.service.AccountsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@WebMvcTest
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = {AccountsController.class, AccountsService.class, GlobalExceptionHandler.class})
public class MockitoControllerAccountTest {


    @MockBean
    private AccountsService accountsService;


    @Test
    void givenAccountsInDatabase_whenListingAccounts_thenReturnAllAccounts() {
        Mockito.when(accountsService.listAccounts())
                .thenReturn(List.of(
                        new Account("1", new BigDecimal("100.0")),
                        new Account("2", new BigDecimal("500.0"))));

        List<Account> accounts = accountsService.listAccounts();

        assertEquals(2, accounts.size());
        assertEquals("1", accounts.get(0).getAccountNumber());
        assertEquals(new BigDecimal("500.0"), accounts.get(1).getAmount());
    }

    @Test
    void givenAccount_whenGetAccountByAccountNumber_thenReturnAccount() {
        Mockito.when(accountsService.getAccountByAccountNumber("1"))
                .thenReturn(new Account("1", new BigDecimal("100.0")));

        Account account = accountsService.getAccountByAccountNumber("1");

        assertEquals(new BigDecimal("100.0"), account.getAmount());
    }

}
