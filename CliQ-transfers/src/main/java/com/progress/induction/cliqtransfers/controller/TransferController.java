package com.progress.induction.cliqtransfers.controller;

import com.progress.induction.cliqtransfers.exception.ApplicationException;
import com.progress.induction.cliqtransfers.model.ResponseResult;
import com.progress.induction.cliqtransfers.model.Transfer;
import com.progress.induction.cliqtransfers.parser.CsvParser;
import com.progress.induction.cliqtransfers.service.TransfersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v1/transfers")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class TransferController {

    private final TransfersService transfersService;
    private final CsvParser csvParser;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public Transfer transfer(@RequestBody Transfer transfer) {
        log.info("--- Start transfer Money controller ---");
        return transfersService.transfer(transfer);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Transfer> listTransfers() {
        log.info("--- Start list Transfers controller ---");
        return transfersService.listTransfers();
    }

    @PostMapping("/upload-csv")
    @ResponseStatus(HttpStatus.OK)
    public ResponseResult transferCSV(@RequestParam("file") MultipartFile file) {

        List<Transfer> transfers = csvParser.readCSVFile(getInputStream(file));
        System.out.println(transfers);
        return  transfersService.transferBulkOfTransfers(transfers);
    }

    private InputStreamReader getInputStream(MultipartFile file) {
        if (file.isEmpty()) {
            throw new ApplicationException("File is empty");
        }
        try {
            return new InputStreamReader(file.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

