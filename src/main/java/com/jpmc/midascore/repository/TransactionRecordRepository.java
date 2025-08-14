package com.jpmc.midascore.repository;

import com.jpmc.midascore.domain.TransactionRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TransactionRecordRepository extends JpaRepository<TransactionRecord, Long>{ }
