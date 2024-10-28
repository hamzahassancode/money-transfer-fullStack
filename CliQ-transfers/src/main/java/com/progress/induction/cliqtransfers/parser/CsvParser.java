package com.progress.induction.cliqtransfers.parser;

import com.progress.induction.cliqtransfers.exception.ApplicationException;
import com.progress.induction.cliqtransfers.model.Transfer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@RequiredArgsConstructor
@Service
public class CsvParser {

    private String[] headers;

    @Getter
    private List<String> invalidRows;
    private List<Transfer> validTransfers;

    public List<Transfer> readCSVFile(InputStreamReader streamReader) {
        invalidRows = new ArrayList<>();
        validTransfers = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(streamReader)) {
            String line;
            String headersLine = reader.readLine();
            headers = validateCsvHeaders(headersLine);

            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    String[] data = line.split(",");
                    validateRecord(data);
                }
            }
            return validTransfers;
        } catch (Exception e) {
            throw new ApplicationException("Error reading CSV file: " + e.getMessage());
        }
    }

    private void validateRecord(String[] data) {
        try {
            if (data.length == headers.length) {
                Transfer transfer = getTransfer(data);
                validTransfers.add(transfer);
            } else {
                addAsInvalid("Some column are missing",data);
            }
        } catch (Exception e) {
            addAsInvalid(e.getMessage(),data);
        }
    }

    private void addAsInvalid(String e,String[] data) {
        if (!(data.length == 0)) {
            invalidRows.add(e +": "+data);
        }
    }

    private Transfer getTransfer(String[] data) {
        Map<String, String> headerMap = new HashMap<>();
        for (int i = 0; i < headers.length; i++) {
            headerMap.put(headers[i].trim(), data[i].trim());
        }

        return new Transfer(headerMap.get("debitAccount"),
                headerMap.get("beneficiary"),
                headerMap.get("beneficiaryType"),
                new BigDecimal(headerMap.get("amount")),
                LocalDate.parse(headerMap.get("valueDate")));
    }

    private String[] validateCsvHeaders(String csvHeaders) throws IllegalArgumentException {
        List<String> REQUIRED_HEADERS = List.of("debitAccount", "beneficiaryType", "beneficiary", "amount", "valueDate");

        String[] headers = csvHeaders.split(",");

        if (headers.length != REQUIRED_HEADERS.size()) {
            throw new ApplicationException("CSV file has an incorrect number of headers.");

        }

        for (int i = 0; i < REQUIRED_HEADERS.size(); i++) {
            if (!REQUIRED_HEADERS.get(i).equals(headers[i].trim())) {
                throw new ApplicationException("CSV headers are not correct. Expected: " + REQUIRED_HEADERS + ", found: " + Arrays.toString(headers));
            }
        }
        return headers;
    }

    public  HashMap<String, String> convertHashmap(HashMap<String, String[]> input) {
        HashMap<String, String> output = new HashMap<>();

        for (String key : input.keySet()) {
            String[] values = input.get(key);
            String concatenatedValue = String.join(", ", values);
            output.put(key, concatenatedValue);
        }

        return output;
    }


}
