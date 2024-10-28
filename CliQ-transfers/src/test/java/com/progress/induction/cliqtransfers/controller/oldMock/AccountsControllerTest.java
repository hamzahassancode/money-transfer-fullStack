package com.progress.induction.cliqtransfers.controller.oldMock;


import com.progress.induction.cliqtransfers.controller.AccountsController;
import com.progress.induction.cliqtransfers.controller.oldMock.mockService.MockAccountService;
import com.progress.induction.cliqtransfers.exception.GlobalExceptionHandler;
import com.progress.induction.cliqtransfers.model.Account;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.List;

@WebMvcTest
@ContextConfiguration(classes = {AccountsController.class, MockAccountService.class, GlobalExceptionHandler.class})
public class AccountsControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MockAccountService mockAccountService;

    @Test
    public void whenListingAccountsFromApi_thenReturnListOfAccounts() throws Exception {
        mockAccountService.setList(
                List.of(new Account("123455", new BigDecimal("2900.0")), new Account("123456", new BigDecimal("1950.0"))));

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/accounts"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].accountNumber", Matchers.is("123455")));
    }

}
