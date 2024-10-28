package com.progress.induction.cliqtransfers.repository;

import com.progress.induction.cliqtransfers.model.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface TransferRepository extends JpaRepository<Transfer, Long> {
    List<Transfer> findByDateLessThanEqualAndStatus(LocalDate date, String status);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = """
        UPDATE transfer t 
        SET lock = :lock,
            locked_until = (CAST(:currentDateTime AS timestamp) + INTERVAL '2 seconds')\s 
        WHERE t.id IN (
         SELECT id FROM transfer 
         where 
            (t.lock IS NULL AND t.status = 'PENDING' AND t.date <= :currentDateTime) 
            or (t.locked_until <= :currentDateTime AND t.status = 'PENDING' AND t.date <= :currentDateTime)
            LIMIT :limit
        )
    """)
    int lockNextBatchForProcessing(@Param("lock") String lock, LocalDateTime currentDateTime,int limit);

    List<Transfer> findAllByLock(String lock);



}
