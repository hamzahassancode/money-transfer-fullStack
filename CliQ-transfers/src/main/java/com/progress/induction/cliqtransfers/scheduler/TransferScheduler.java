package com.progress.induction.cliqtransfers.scheduler;

import com.progress.induction.cliqtransfers.constants.Status;
import com.progress.induction.cliqtransfers.model.Transfer;
import com.progress.induction.cliqtransfers.repository.TransferRepository;
import com.progress.induction.cliqtransfers.service.TransfersService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.repository.Query;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class TransferScheduler {

    private final TransferRepository transferRepository;
    private final TransfersService transferService;

    @Transactional
    @Scheduled(cron = "0 0 0 * * ?")
    protected void scheduleTransfer() {
        List<Transfer> pendingTransfers = updateAndGenerateUID();
        for (Transfer pendingTransfer : pendingTransfers) {
            processPendingTransfers(pendingTransfer);
        }
    }


    private void processPendingTransfers(Transfer pendingTransfer) {
        try{
            transferService.processTransfer(pendingTransfer);
            transferRepository.save(pendingTransfer);
            log.info("Processed pending transfer: {}", pendingTransfer);
        }catch (Exception e) {
            pendingTransfer.setStatus(Status.FAILED);
            transferRepository.save(pendingTransfer);
            log.error("Error processing transfer: {}", pendingTransfer, e);
        }
    }

    private List<Transfer> updateAndGenerateUID(){
        String id=UUID.randomUUID().toString();
        int lockedRows=transferRepository.lockNextBatchForProcessing(id, LocalDateTime.now(),10);
        return transferRepository.findAllByLock(id);

    }
}
