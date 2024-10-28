package com.progress.induction.cliqtransfers.model;

import lombok.Data;

import java.util.HashMap;
import java.util.List;

@Data
public class ResponseResult {
    private String message;
    private int totalErrors;
    private List<String> errorInParsingResults;
    private  List<String> errorInTransfersResults;

    public ResponseResult(int totalErrors, List<String> errorInParsingResults, List<String> errorInTransfersResults) {
       this.totalErrors = totalErrors;
        this.errorInParsingResults=errorInParsingResults;
        this.errorInTransfersResults =errorInTransfersResults;
        this.message= generateMessage();
    }

    private String generateMessage() {
        assert errorInParsingResults != null;
        int errorInParsing = errorInParsingResults.size();
        assert errorInTransfersResults != null;
        int errorInTransfer = errorInTransfersResults.size();
        int totalErrors = errorInParsing + errorInTransfer;

        if (totalErrors > 0) {
            message = "All done. ";

            if (errorInParsing > 0 && errorInTransfer > 0) {
                message += "However, there were errors in both parsing and transferring.";
            } else if (errorInParsing > 0) {
                message += "However, there were errors in parsing the input data.";
            } else if (errorInTransfer > 0) {
                message += "However, there were errors in transferring the data.";
            }
        } else {
            message = "All operations completed successfully. No errors were found.";
        }
    return message;
    }

    static class MessageValue{
        private String message;
        private String row;
    }
}
