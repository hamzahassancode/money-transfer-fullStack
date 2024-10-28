package com.progress.induction.cliqtransfers.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.progress.induction.cliqtransfers.constants.Status;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
public class Transfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String debitAccount;
    private String beneficiaryAccount;
    private String beneficiaryType;
    private LocalDate date;
    private BigDecimal amount;
    private String currency = "JOD";
    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;
    @JsonIgnore
    private String lock;
    @JsonIgnore
    private LocalDateTime lockedUntil;


    public Transfer(String debitAccount, String beneficiaryAccount, String beneficiaryType, BigDecimal amount, LocalDate date) {
        this.debitAccount = debitAccount;
        this.beneficiaryAccount = beneficiaryAccount;
        this.beneficiaryType = beneficiaryType;
        this.amount = amount;
        this.date = date;
    }

    @Override
    public String toString() {
        return
                "id=" + id +
                        ", debitAccount='" + debitAccount + '\'' +
                        ", beneficiaryAccount='" + beneficiaryAccount + '\'' +
                        ", beneficiaryType='" + beneficiaryType + '\'' +
                        ", date=" + date +
                        ", amount=" + amount +
                        ", currency='" + currency + '\'' +
                        ", status='" + status + '\'';
    }

    public String generateStringTransfer() {
        return debitAccount + "," + beneficiaryType + "," + beneficiaryAccount + "," + amount + "," + date;
    }
}

