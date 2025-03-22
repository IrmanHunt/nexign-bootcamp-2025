package com.romanlotohin.nexign_bootcamp_2025.repository;

import com.romanlotohin.nexign_bootcamp_2025.entity.CdrRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface CdrRecordRepository extends JpaRepository<CdrRecord, Long> {
    @Query("SELECT c FROM CdrRecord c " +
            "WHERE (c.callerMsisdn = :msisdn OR c.calleeMsisdn = :msisdn) " +
            "AND c.callStart >= :start AND c.callStart < :end")
    List<CdrRecord> findByMsisdnAndCallStartBetween(@Param("msisdn") String msisdn,
                                                    @Param("start") LocalDateTime callStart,
                                                    @Param("end") LocalDateTime callEnd);
}