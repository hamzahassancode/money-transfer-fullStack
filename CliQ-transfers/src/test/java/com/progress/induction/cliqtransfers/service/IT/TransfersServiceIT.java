package com.progress.induction.cliqtransfers.service.IT;

import com.progress.induction.cliqtransfers.exception.ApplicationException;
import com.progress.induction.cliqtransfers.exception.NotFoundException;
import com.progress.induction.cliqtransfers.model.Account;
import com.progress.induction.cliqtransfers.model.Transfer;
import com.progress.induction.cliqtransfers.repository.AccountRepository;
import com.progress.induction.cliqtransfers.repository.TransferRepository;
import com.progress.induction.cliqtransfers.service.TransfersService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static com.progress.induction.cliqtransfers.Constants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class TransfersServiceIT {


    @Autowired
    private TransferRepository transferRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransfersService transfersService;

    @Test
    void givenValidTransfer_whenTransfer_thenReturnTransfer() {
        Transfer transferRequestDetails = new Transfer(validAccountNumber, validPhoneNumber, phoneNumberMethod, new BigDecimal("1.0"), LocalDate.now());

        assertEquals(validAccountNumber, transfersService.transfer(transferRequestDetails).getDebitAccount());
        assertEquals(new BigDecimal("1.0"), transfersService.transfer(transferRequestDetails).getAmount());
        assertEquals(validPhoneNumber, transfersService.transfer(transferRequestDetails).getBeneficiaryAccount());
    }

    @BeforeEach
    void setUp() {
        accountRepository.save(new Account("0000122204115301", new BigDecimal("500.0")));
    }

    @Test
    void givenAccountNotAvailableInDB_whenTransfer_thenThrowException() {
        Transfer transferRequestDetails = new Transfer("invalid Account Number", validPhoneNumber, phoneNumberMethod, new BigDecimal("1.0"), LocalDate.now());

        NotFoundException ex = assertThrows(NotFoundException.class, () -> transfersService.transfer(transferRequestDetails));

        assertEquals("This Account Does Not Exist", ex.getMessage());
    }

    @Test
    void givenInValidBalance_whenTransfer_thenThrowException() {
        Transfer transferRequestDetails = new Transfer(validAccountNumber, validPhoneNumber, phoneNumberMethod, new BigDecimal("6000.0"), LocalDate.now());

        ApplicationException ex = assertThrows(ApplicationException.class, () -> transfersService.transfer(transferRequestDetails));

        assertEquals("You can't transfer this amount", ex.getMessage());
    }

    @Test
    void givenNotEnoughBalance_whenTransfer_thenThrowException() {
        Transfer transferRequestDetails = new Transfer(validAccountNumber, validPhoneNumber, phoneNumberMethod, new BigDecimal("600.0"), LocalDate.now());

        ApplicationException ex = assertThrows(ApplicationException.class, () -> transfersService.transfer(transferRequestDetails));

        assertEquals("You don't have enough balance", ex.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "0793652450258", "0096278523", "0096273874413902", "00962712365478963"})
    void givenInValidPhoneNumberAsBeneficiary_whenTransfer_thenThrowException(String phoneNumber) {
        Transfer transferRequestDetails = new Transfer(validAccountNumber, phoneNumber, phoneNumberMethod, new BigDecimal("1.0"), LocalDate.now());

        ApplicationException ex = assertThrows(ApplicationException.class, () -> transfersService.transfer(transferRequestDetails));

        assertEquals("Invalid Phone Number", ex.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "HA0078944563456336544563365445", "JO8590078944563456336544563365445", "JO0078944563456336544563365"})
    void givenInValidIBAN_whenTransfer_thenThrowException(String IBAN) {
        String IBANMethod = "IBAN";
        Transfer transferRequestDetails = new Transfer(validAccountNumber, IBAN, IBANMethod, new BigDecimal("1.0"), LocalDate.now());

        ApplicationException ex = assertThrows(ApplicationException.class, () -> transfersService.transfer(transferRequestDetails));

        assertEquals("Invalid IBAN", ex.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "J078944563456336544563365445", "8590078"})
    void givenInValidAlias_whenTransfer_thenThrowException(String alias) {
        Transfer transferRequestDetails = new Transfer(validAccountNumber, alias, aliasMethod, new BigDecimal("1.0"), LocalDate.now());

        ApplicationException ex = assertThrows(ApplicationException.class, () -> transfersService.transfer(transferRequestDetails));

        assertEquals("Invalid Alias", ex.getMessage());
    }

    @Test
    void givenValidBeneficiaryButInvalidBeneficiaryType_whenTransfer_thenThrowException() {
        ApplicationException ex = assertThrows(ApplicationException.class, () -> {
            Transfer transferRequestDetails =
                    new Transfer(validAccountNumber, validPhoneNumber, "INVALID TYPE ", new BigDecimal("1.0"), LocalDate.now());

            transfersService.transfer(transferRequestDetails);
        });

        assertEquals("Invalid beneficiary type. Choose IBAN, Phone, or Alias.", ex.getMessage());
    }

    @Test
    void givenValidBeneficiaryAndValidBeneficiaryTypeButTheyAreMissMatch_whenTransfer_thenThrowException() {
        ApplicationException ex = assertThrows(ApplicationException.class, () -> {

            Transfer transferRequestDetails =
                    new Transfer(validAccountNumber, validPhoneNumber, aliasMethod, new BigDecimal("1.0"), LocalDate.now());
            transfersService.transfer(transferRequestDetails);
        });

        assertEquals("Invalid Alias", ex.getMessage());
    }

    @Test
    void givenValidListOfTransfers_whenListingTransfers_thenListOfTransfers() {
        transferRepository.save(new Transfer(validAccountNumber, validPhoneNumber, aliasMethod, new BigDecimal("1.0"), LocalDate.now()));

        List<Transfer> result = transfersService.listTransfers();

        assertEquals(1, result.size());
        assertEquals(new BigDecimal("1.0"), result.get(0).getAmount());
        assertEquals("0000122204115301", result.get(0).getDebitAccount());
        assertEquals("00962798713902", result.get(0).getBeneficiaryAccount());
    }

}

