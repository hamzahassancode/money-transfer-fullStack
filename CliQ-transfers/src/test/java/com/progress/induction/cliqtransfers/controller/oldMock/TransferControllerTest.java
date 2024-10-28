//package com.progress.induction.cliqtransfers.controller.oldMock;
//
//import com.progress.induction.cliqtransfers.controller.TransferController;
//import com.progress.induction.cliqtransfers.controller.oldMock.mockService.MockTransferService;
//import com.progress.induction.cliqtransfers.exception.GlobalExceptionHandler;
//import org.hamcrest.Matchers;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//
//import java.time.LocalDate;
//
//@WebMvcTest
//@ContextConfiguration(classes = {TransferController.class, MockTransferService.class , GlobalExceptionHandler.class})
//class TransferControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Test
//    public void givenValidTransferRequestDetails_whenTransfer_thenReturnTransfer() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/transfers").contentType(MediaType.APPLICATION_JSON).content(
//               """
//               {
//                    "debitAccount":"0000122204115301",
//                    "amount":42,
//                    "beneficiaryAccount":"Shahed123",
//                    "bene   ficiaryType":"Alias"
//                }
//                """))
//
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.debitAccount", Matchers.is("0000122204115301")))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.beneficiaryAccount", Matchers.is("Shahed123")))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.date", Matchers.is("2023-04-09")))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.currency", Matchers.is("JOD")))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.amount", Matchers.is(42)));
//
//
//
//    }
//    @Test
//    public void givenInValidRequestDetails_whenTransfer_thenReturnBadRequestAndMessage() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/transfers").contentType(MediaType.APPLICATION_JSON).content(
//                """
//                {
//                     "debitAccount":"0000122204115301",
//                     "amount":600000,
//                     "beneficiaryAccount":"Shaed123",
//                     "beneficiaryType":"Alias"
//                 }
//                 """
//                ))
//
//                .andExpect(MockMvcResultMatchers.status().isBadRequest())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("You Can't Transfer This Amount")));
//    }
//
//    @Test
//    public void whenListingTransfers_thenReturnListOfTransfers() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/transfers")).andExpect(
//                MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1))
//        )
//                .andExpect(MockMvcResultMatchers.jsonPath("$[0].debitAccount", Matchers.is("0000122204115301")))
//                .andExpect(MockMvcResultMatchers.jsonPath("$[0].beneficiaryAccount", Matchers.is("Shahed123")))
//                .andExpect(MockMvcResultMatchers.jsonPath("$[0].date", Matchers.is(LocalDate.now().toString())))
//                .andExpect(MockMvcResultMatchers.jsonPath("$[0].currency", Matchers.is("JOD")));
//
//
//    }
//
//}