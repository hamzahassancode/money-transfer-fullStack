package com.progress.induction.cliqtransfers.service.mock;//package com.progress.induction.cliqtransfers.service.mock;

import com.progress.induction.cliqtransfers.constants.Status;
import com.progress.induction.cliqtransfers.exception.ApplicationException;
import com.progress.induction.cliqtransfers.exception.NotFoundException;
import com.progress.induction.cliqtransfers.model.Account;
import com.progress.induction.cliqtransfers.model.ResponseResult;
import com.progress.induction.cliqtransfers.model.Transfer;
import com.progress.induction.cliqtransfers.parser.CsvParser;
import com.progress.induction.cliqtransfers.repository.AccountRepository;
import com.progress.induction.cliqtransfers.repository.TransferRepository;
import com.progress.induction.cliqtransfers.service.TransfersService;
import com.progress.induction.cliqtransfers.service.validator.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.progress.induction.cliqtransfers.Constants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransferServiceTestMockito {


    private Account validAccount;

    private Validator validator;

    @Mock
    private TransferRepository transferRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private CsvParser csvParser;

    @InjectMocks
    private TransfersService transfersService;


    @BeforeEach
    void setup() {

        // Initialize real validators
        validator = new Validator(accountRepository);

        // Inject real validators into the service
        transfersService = new TransfersService(validator, accountRepository, transferRepository, csvParser);
        validAccount = new Account(validAccountNumber, new BigDecimal("100.0"));
    }

    @Test
    void givenAccountNotAvailableInDB_whenTransfer_thenThrowException() {
        when(accountRepository.findById("invalid Account Number")).thenThrow(new NotFoundException("This Account Does Not Exist"));

        NotFoundException ex = assertThrows(NotFoundException.class, () -> transfersService.transfer(
                new Transfer("invalid Account Number", validPhoneNumber, phoneNumberMethod, new BigDecimal("10.0"), LocalDate.now())));
        assertEquals("This Account Does Not Exist", ex.getMessage());
    }


    @Test
    void givenInValidBalance_whenTransfer_thenThrowException() {
        Transfer transferRequestInvalidAmount =
                new Transfer(validAccountNumber, validPhoneNumber, phoneNumberMethod, new BigDecimal("10000"), LocalDate.now());
        ApplicationException ex = assertThrows(ApplicationException.class, () -> transfersService.transfer(transferRequestInvalidAmount));

        assertEquals("You can't transfer this amount", ex.getMessage());
    }


    @Test
    void givenInsufficientBalance_whenTransfer_thenThrowException() {

        when(accountRepository.findById(validAccountNumber)).thenReturn(Optional.of(VALID_ACCOUNT_1));
        Transfer transferRequestInvalidAmount =
                new Transfer(validAccountNumber, validPhoneNumber, phoneNumberMethod, new BigDecimal("1000"), LocalDate.now());
        ApplicationException ex = assertThrows(ApplicationException.class, () -> transfersService.transfer(transferRequestInvalidAmount));

        assertEquals("You don't have enough balance", ex.getMessage());
    }

    @Test
    void givenInvalidBeneficiaryAccountMethod_whenTransfer_thenThrowException() {

        Transfer transferRequestInvalidAmount =
                new Transfer(validAccountNumber, validPhoneNumber, "INVALID", new BigDecimal("100"), LocalDate.now());
        ApplicationException ex = assertThrows(ApplicationException.class, () -> transfersService.transfer(transferRequestInvalidAmount));

        assertEquals("Invalid beneficiary type. Choose IBAN, Phone, or Alias.", ex.getMessage());
    }

    @Test
    void givenInvalidBeneficiaryAccount_whenTransfer_thenThrowException() {

        Transfer transferRequestInvalidAmount =
                new Transfer(validAccountNumber, validPhoneNumber, aliasMethod, new BigDecimal("100"), LocalDate.now());
        ApplicationException ex = assertThrows(ApplicationException.class, () -> transfersService.transfer(transferRequestInvalidAmount));

        assertEquals("Invalid Alias", ex.getMessage());
    }

    @Test
    void givenValidTransfer_whenProcessTransfer_thenReturnCompleted() {
        Transfer transfer = new Transfer(validAccountNumber, "user", aliasMethod, new BigDecimal("10"), LocalDate.now());
        when(accountRepository.findById(validAccountNumber)).thenReturn(Optional.of(VALID_ACCOUNT_1));

        transfersService.processTransfer(transfer);

        assertEquals(Status.COMPLETED, transfer.getStatus());
        verify(accountRepository, times(2)).findById(validAccountNumber);
    }


    @Test
    void testListTransfers() {
        List<Transfer> transfers = List.of(new Transfer(), new Transfer());
        when(transferRepository.findAll()).thenReturn(transfers);

        List<Transfer> result = transfersService.listTransfers();

        assertEquals(2, result.size());
        verify(transferRepository, times(1)).findAll();
    }


    @Test
    void givenListOfTransfer_whenTransferBulkOfTransfers_thenReturnResponseResult() throws Exception {
        List<Transfer> transfers = List.of(
                new Transfer(validAccountNumber, "user", aliasMethod, ONE_JOD, LocalDate.of(2077, 05, 28)),
                new Transfer(validAccountNumber, "TEST2", aliasMethod,ONE_JOD, LocalDate.now()),
                new Transfer(validAccountNumber, "TEST2", aliasMethod, ONE_JOD, LocalDate.of(2079, 10, 27)),
                new Transfer(validAccountNumber, "TEST2", aliasMethod, ONE_JOD, LocalDate.of(2000, 10, 27)),
                new Transfer("000012220411530555555555555552", "TEST2", aliasMethod, ONE_JOD, LocalDate.now()),
                new Transfer(validAccountNumber, "TEST2", aliasMethod, new BigDecimal("1000000"), LocalDate.now()));

        List<String> errorInTransfersResults = new ArrayList<>() {{
            add("Date cannot be in the past.: 0000122204115301,Alias,TEST2,1.1,2000-10-27");
            add("This Account Does Not Exist"+": "+"000012220411530555555555555552,Alias,TEST2,1.1," + LocalDate.now());
            add("You can't transfer this amount" +": "+  "0000122204115301,Alias,TEST2,1000000,"+ LocalDate.now());
        }};

        ResponseResult expectedResponse = new ResponseResult(3, new ArrayList<>(), errorInTransfersResults);
        when(accountRepository.findById(validAccountNumber)).thenReturn(Optional.of(VALID_ACCOUNT_1));
        ResponseResult actualResponse = transfersService.transferBulkOfTransfers(transfers);

        // Assert
        assertEquals(actualResponse.getTotalErrors(),3);
        assertEquals(expectedResponse, actualResponse);
    }


}
