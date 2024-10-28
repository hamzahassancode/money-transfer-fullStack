package com.progress.induction.cliqtransfers.service.mock;

import com.progress.induction.cliqtransfers.exception.NotFoundException;
import com.progress.induction.cliqtransfers.model.Account;
import com.progress.induction.cliqtransfers.repository.AccountRepository;
import com.progress.induction.cliqtransfers.service.AccountsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.progress.induction.cliqtransfers.Constants.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTestMockito {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountsService accountsService;

    @Test
    public void whenListingAccounts_thenReturnListOfAccounts() {
        Mockito.when(accountRepository.findAll()).thenReturn(listOfValidAccounts);

        List<Account> accounts = accountsService.listAccounts();


        assertEquals(listOfValidAccounts,  accounts);
        assertEquals(3, accounts.size());
        Mockito.verify(accountRepository, Mockito.times(1)).findAll();
    }


    @Test
    public void givenAccountNUMBER_whenGetAccountByAccountNumber_thenReturnAccount() {
        Mockito.when(accountRepository.findById("1")).thenReturn(Optional.of(VALID_ACCOUNT_1));

        Account result = accountsService.getAccountByAccountNumber("1");

        assertEquals(VALID_ACCOUNT_1, result);
        assertEquals(new BigDecimal("100.0"), result.getAmount());
        Mockito.verify(accountRepository).findById("1");
    }

    @Test
    public void givenNonExeistAccountNumber_whenGetAccountByAccountNumber_thenReturnAccount() {
        Mockito.when(accountRepository.findById("10")).thenThrow(new NotFoundException("no account found"));

        assertThrows(NotFoundException.class,()->accountsService.getAccountByAccountNumber("10"));
    }

}
