package com.progress.induction.cliqtransfers.service.validator;

import com.progress.induction.cliqtransfers.exception.ApplicationException;
import com.progress.induction.cliqtransfers.exception.NotFoundException;
import com.progress.induction.cliqtransfers.model.Account;
import com.progress.induction.cliqtransfers.model.Transfer;
import com.progress.induction.cliqtransfers.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.IBANValidator;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@RequiredArgsConstructor
@Component
@Slf4j
public class Validator {

    private final AccountRepository accountRepository;

    public void validateTransferDetails(Transfer transfer) {
        validateDate(transfer.getDate());
        validateBeneficiary(transfer.getBeneficiaryAccount(), transfer.getBeneficiaryType());
        validateAmountInAcceptableRange(transfer.getAmount());
        log.info("--- Valid beneficiary Account ---");
    }
    public void verifyDebitAccountAndBalance(Transfer transfer) {
        Account account = validateDebitAccount(transfer.getDebitAccount());
        validateSufficientAmount(transfer.getAmount(), account);
    }


    private void validateAmountInAcceptableRange(BigDecimal amount) {
        final BigDecimal lowerLimit = BigDecimal.valueOf(1);
        final BigDecimal upperLimit = BigDecimal.valueOf(5000);

        if (amount.compareTo(lowerLimit) < 0 || amount.compareTo(upperLimit) > 0) {
            log.warn("--- Invalid transfer amount: {}. It must be between 1 and 5000 ---", amount);
            throw new ApplicationException("You can't transfer this amount");
        }
    }

    private void validateSufficientAmount(BigDecimal amount, Account account) {
        if (amount.compareTo(account.getAmount()) > 0) {
            log.warn("--- Insufficient balance. Account balance: {}, attempted transfer: {} ---", account.getAmount(), amount);
            throw new ApplicationException("You don't have enough balance");
        }
    }


    private Account validateDebitAccount(String sourceAccount) {
        return accountRepository.findById(sourceAccount).orElseThrow(() -> {
            log.warn("--- Account {} does not exist---", sourceAccount);
            return new NotFoundException("This Account Does Not Exist");
        });
    }

    private void validateBeneficiary(String beneficiaryString, String beneficiaryType) {
        log.info("--- Validating beneficiary with type: {} ---", beneficiaryType);

        switch (beneficiaryType) {
            case "IBAN":
                validateIBAN(beneficiaryString);
                break;
            case "Phone Number":
                validatePhone(beneficiaryString);
                break;
            case "Alias":
                validateAlias(beneficiaryString);
                break;
            default:
                log.error("--- Invalid beneficiary type: {} ---", beneficiaryType);
                throw new ApplicationException("Invalid beneficiary type. Choose IBAN, Phone, or Alias.");
        }

        log.info("--- Beneficiary validation successful for type: {} ---", beneficiaryType);
    }

    private void validateDate(LocalDate date) {
        if (date.isBefore(LocalDate.now())) {
            throw new ApplicationException("Date cannot be in the past.");
        }
    }

    private boolean isValidJordanianIBAN(String iban) {
        log.debug("--- Checking if IBAN is valid: {} ---", iban);
        IBANValidator ibanValidator = new IBANValidator();
        boolean isValid = ibanValidator.isValid(iban) && iban.startsWith("JO") && iban.length() == 30;
        log.info("--- IBAN validation result for {}: {} ---", iban, isValid);
        return isValid;
    }

    private boolean isValidJordanianMobile(String phoneNumber) {
        log.debug("--- Checking if Phone Number is valid: {} ---", phoneNumber);
        boolean isValid = phoneNumber != null && phoneNumber.matches("^009627\\d{8}$");
        log.info("---Phone Number validation result for {}: {} ---", phoneNumber, isValid);
        return isValid;
    }

    private boolean isValidAlias(String alias) {
        log.debug("--- Checking if Alias is valid: {} ---", alias);
        boolean isValid = alias != null && alias.matches("^[a-zA-Z][a-zA-Z0-9]{0,19}$");
        log.info("--- Alias validation result for {}: {} ---", alias, isValid);
        return isValid;
    }

    private void validateIBAN(String iban) {
        if (!isValidJordanianIBAN(iban)) {
            log.error("--- Invalid IBAN: {} ---", iban);
            throw new ApplicationException("Invalid IBAN");
        }
    }

    private void validatePhone(String phone) {
        if (!isValidJordanianMobile(phone)) {
            log.error("--- Invalid Phone Number: {}---", phone);
            throw new ApplicationException("Invalid Phone Number");
        }
    }

    private void validateAlias(String alias) {
        if (!isValidAlias(alias)) {
            log.error("--- Invalid Alias: {} ---", alias);
            throw new ApplicationException("Invalid Alias");
        }
    }
}


