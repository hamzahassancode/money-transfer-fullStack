package com.progress.induction.cliqtransfers.controller.mockito;

import com.progress.induction.cliqtransfers.controller.TransferController;
import com.progress.induction.cliqtransfers.exception.ApplicationException;
import com.progress.induction.cliqtransfers.exception.GlobalExceptionHandler;
import com.progress.induction.cliqtransfers.model.ResponseResult;
import com.progress.induction.cliqtransfers.model.Transfer;
import com.progress.induction.cliqtransfers.parser.CsvParser;
import com.progress.induction.cliqtransfers.service.TransfersService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;

@WebMvcTest
@ContextConfiguration(classes = {TransferController.class, GlobalExceptionHandler.class})
public class MockitoControllerTransferTest {

    private static final String API_URL = "/v1/transfers";
    private final String validAccountNumber = "0000122204115301";

    @MockBean
    private CsvParser csvParser;

    @MockBean
    private TransfersService transfersService;

    @Autowired
    private MockMvc mockMvc;

    private Transfer validTransfer;
    @Autowired
    private TransferController transferController;

    @BeforeEach
    void setup() {
        validTransfer = new Transfer(validAccountNumber, "userTest", "Alias", new BigDecimal("42.0"), LocalDate.of(2024, 10, 26));
        Transfer invalidTransfer = new Transfer(validAccountNumber, "userTest", "Alias", new BigDecimal("500000"), LocalDate.of(2024, 10, 26));
        validTransfer.setId(1L);
        invalidTransfer.setId(1L);
    }

    @Test
    public void givenValidTransferRequest_whenTransfer_thenReturnTransfer() throws Exception {
        when(transfersService.transfer(any(Transfer.class))).thenReturn(validTransfer);

        String validTransferRequestJson = """
            {
                "debitAccount": "0000122204115301",
                "amount": "42.0",
                "beneficiaryAccount": "userTest",
                "beneficiaryType": "Alias",
                "date": "2024-10-26"
            }
            """;

        ResultActions resultActions = performPostRequest(validTransferRequestJson);

        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.debitAccount", Matchers.is(validAccountNumber)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.beneficiaryAccount", Matchers.is("userTest")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.date", Matchers.is("2024-10-26")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.amount", Matchers.is(42.0)));

        Mockito.verify(transfersService, times(1)).transfer(any(Transfer.class));
    }

    @Test
    public void givenInvalidAmountRequest_whenTransfer_thenReturnBadRequest() throws Exception {
        when(transfersService.transfer(any(Transfer.class)))
                .thenThrow(new ApplicationException("You Can't Transfer This Amount"));

        String invalidTransferRequestJson = """
            {
                "debitAccount": "0000122204115301",
                "amount": "500000",
                "beneficiaryAccount": "userTest",
                "beneficiaryType": "Alias",
                "date": "2024-10-26"
            }
            """;

        ResultActions resultActions = performPostRequest(invalidTransferRequestJson);

        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("You Can't Transfer This Amount")));
    }



    @Test
    public void whenListingTransfers_thenReturnListOfTransfers() throws Exception {
        when(transfersService.listTransfers()).thenReturn(List.of(validTransfer));

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get(API_URL));

        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].debitAccount", Matchers.is(validAccountNumber)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].beneficiaryAccount", Matchers.is("userTest")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].date", Matchers.is("2024-10-26")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].amount", Matchers.is(42.0)));
    }

    private ResultActions performPostRequest(String requestBody) throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);

        return mockMvc.perform(requestBuilder);
    }

    /*
    * new
    *
    * */
//
//    @Test
//    void testTransferCSV() throws Exception {
//        MockMultipartFile file = getMockMultipartFile();
//
//        HashMap<String, String> errorInTransfersResults = new HashMap<>(){{
//            put("This Account Does Not Exist", "000012220411530555555555555552,Alias,TEST2,1.1,2024-10-27,");
//        }};
//        ResponseResult expectedResponse = new ResponseResult(1, new HashMap<>(),errorInTransfersResults);
//        List<Transfer> transfers = List.of(
//                new Transfer("0000122204115302", "TEST1", "Alias", new BigDecimal("1.1"), LocalDate.of(2025, 05, 28)),
//                new Transfer("0000122204115302", "TEST2", "Alias", new BigDecimal("1.1"), LocalDate.of(2024, 10, 27)),
//                new Transfer("0000122204115302", "TEST2", "Alias", new BigDecimal("2.1"), LocalDate.of(2025, 10, 27)),
//                new Transfer("000012220411530555555555555552", "Alias", "TEST2", new BigDecimal("1.1"), LocalDate.of(2024, 10, 27))
//
//        );
//        when(csvParser.readCSVFile(any(InputStreamReader.class))).thenReturn(transfers); // Replace with your expected parsed data
//        when(transfersService.transferBulkOfTransfers(transfers)).thenReturn(expectedResponse);
//        // Act
//        ResponseResult actualResponse = transferController.transferCSV(file);
//        // Assert
//        assertEquals(expectedResponse, actualResponse); // Adjust according to your equals method
//    }

    private static MockMultipartFile getMockMultipartFile() {
        String csvContent = "debitAccount,beneficiaryType,beneficiary,amount,valueDate" +
                            "\n0000122204115302,Alias,TEST1,1.1,2025-05-28" +
                            "\n0000122204115302,Alias,TEST2,1.1,2024-10-27"+
                            "\n0000122204115302,Alias,TEST2,2.1,2024-10-27" +
                            "\n000012220411530555555555555552,Alias,TEST2,1.1,2024-10-27";

        return new MockMultipartFile("file", "test.csv", "text/csv", csvContent.getBytes());
    }

    @Test
    void whenBulkTransfer_thenParseFileAndReturnBulkResponse() throws Exception {
        MockMultipartFile file = getMockMultipartFile();

        ResponseResult expectedResponse = new ResponseResult(1, List.of(),List.of());

        when(csvParser.readCSVFile(any(InputStreamReader.class))).thenReturn(List.of());
        when(transfersService.transferBulkOfTransfers(List.of())).thenReturn(expectedResponse);

        mockMvc.perform(multipart("/v1/transfers/upload-csv").file(file).contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(MockMvcResultMatchers.status().isOk());
        verify(transfersService, times(1)).transferBulkOfTransfers(List.of());
        verify(csvParser, times(1)).readCSVFile(any(InputStreamReader.class));
    }

}
