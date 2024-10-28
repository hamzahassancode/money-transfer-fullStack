package com.progress.induction.cliqtransfers.controller.oldMock.mockService;

import com.progress.induction.cliqtransfers.exception.NotFoundException;
import com.progress.induction.cliqtransfers.model.Account;
import com.progress.induction.cliqtransfers.service.AccountsService;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Setter
@Getter
public class MockAccountService extends AccountsService {

    private List<Account> list ;

    public MockAccountService() {
        super(null);
        list = new ArrayList<>();
    }

    @Override
    public List<Account> listAccounts() {
        if(list.isEmpty()){

            throw new NotFoundException("No accounts found");
        }
        return list;
    }
}
