//package com.progress.induction.cliqtransfers.controller.oldMock.mockService;
//
//
//import com.progress.induction.cliqtransfers.exception.ApplicationException;
//import com.progress.induction.cliqtransfers.model.Transfer;
//import com.progress.induction.cliqtransfers.service.TransfersService;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.util.List;
//
//public class MockTransferService extends TransfersService {
//
//    public MockTransferService() {
//        super(null, null, null, null);
//    }
//
//    @Override
//    public Transfer transfer(Transfer transfer) {
//        if (transfer.getAmount().compareTo(new BigDecimal("5000.0")) > 0) {
//            throw new ApplicationException("You Can't Transfer This Amount");
//        }
//        Transfer result = new Transfer(
//                transfer.getDebitAccount(),
//                transfer.getBeneficiaryAccount(),
//                transfer.getBeneficiaryType(),
//                transfer.getAmount(),
//                transfer.getDate());
//        result.setId(1L);
//        return result;
//    }
//
//    @Override
//    public List<Transfer> listTransfers() {
//
//        return List.of(new Transfer("0000122204115301", "Shahed123","Alias", new BigDecimal("1.0"), LocalDate.parse("2024-10-22")));
//    }
//}
