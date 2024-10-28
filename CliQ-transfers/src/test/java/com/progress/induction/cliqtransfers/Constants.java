package com.progress.induction.cliqtransfers;

import com.progress.induction.cliqtransfers.model.Account;

import java.math.BigDecimal;
import java.util.List;

public class Constants {
    public static final String validPhoneNumber = "00962798713902";
    public static final  String phoneNumberMethod = "Phone Number";
    public static final String aliasMethod = "Alias";
    public static final BigDecimal ONE_JOD=new BigDecimal("1.1");
    public static final String validAccountNumber = "0000122204115301";
    public static final  Account VALID_ACCOUNT_1 =new Account("0000122204115301", new BigDecimal("100.0"));
    public static final  Account VALID_ACCOUNT_2 =new Account("0000122204115302", new BigDecimal("200.0"));
    public static final  Account VALID_ACCOUNT_3 =new Account("0000122204115303", new BigDecimal("300.0"));
    public static final List<Account> listOfValidAccounts = List.of(VALID_ACCOUNT_1, VALID_ACCOUNT_2, VALID_ACCOUNT_3);
}
