package com.progress.induction.cliqtransfers.service;

import com.progress.induction.cliqtransfers.exception.ApplicationException;
import com.progress.induction.cliqtransfers.exception.NotFoundException;
import com.progress.induction.cliqtransfers.model.ResponseResult;
import com.progress.induction.cliqtransfers.constants.Status;
import com.progress.induction.cliqtransfers.model.Transfer;
import com.progress.induction.cliqtransfers.parser.CsvParser;
import com.progress.induction.cliqtransfers.repository.AccountRepository;
import com.progress.induction.cliqtransfers.repository.TransferRepository;
import com.progress.induction.cliqtransfers.service.validator.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class TransfersService {
    private final Validator validator;
    private final AccountRepository accountRepository;
    private final TransferRepository transferRepository;
    private List<String> inValidTransfers;
    private final CsvParser csvParser;

    public Transfer transfer(Transfer transfer) {
        validator.validateTransferDetails(transfer);
        if (transfer.getDate().equals(LocalDate.now())) {
            processTransfer(transfer);
        }
        saveTransfer(transfer);
        return transfer;
    }

    public ResponseResult transferBulkOfTransfers(List<Transfer> transfers) {
        inValidTransfers = new ArrayList<>();
        transfers.forEach((transfer) -> {
            try {
                transfer(transfer);
            } catch (ApplicationException | NotFoundException e) {
                inValidTransfers.add(e.getMessage()+": "+transfer.generateStringTransfer());
            }
        });
        int totalErrors=inValidTransfers.size() + csvParser.getInvalidRows().size();

        return new ResponseResult(totalErrors,csvParser.getInvalidRows(), inValidTransfers);
    }




    public void processTransfer(Transfer transfer) {
        validator.verifyDebitAccountAndBalance(transfer);
        updateDebitAccountAmount(transfer.getDebitAccount(), transfer.getAmount());
        transfer.setStatus(Status.COMPLETED);
        log.info("--- Transfer success ---");

    }

    public List<Transfer> listTransfers() {
        List<Transfer> result = transferRepository.findAll();
        log.info("--- Display list transfers from DB---");
        log.info("--- {} ---", result);
        return result;
    }


    private void updateDebitAccountAmount(String debitAccount, BigDecimal amount) {
        accountRepository.findById(debitAccount).ifPresent(account -> {
            BigDecimal newAmount = account.getAmount().subtract(amount);
            account.setAmount(newAmount);
            accountRepository.save(account);
        });
        log.info("--- debit account balance updated ---");
    }

    private void saveTransfer(Transfer transfer) {
        transferRepository.save(transfer);
        log.info("--- Transaction saved in DB---");
    }
}
