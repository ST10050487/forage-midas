package com.jpmc.midascore.kafka;

import com.jpmc.midascore.service.TransactionProcessingService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import com.jpmc.midascore.foundation.Transaction;

@Component
public class KafkaTransactionListener {

    private final TransactionProcessingService processingService;

    public KafkaTransactionListener(TransactionProcessingService processingService) {
        this.processingService = processingService;
    }

    @KafkaListener(topics = "${general.kafka-topic}", groupId = "midas-core")
    public void onTransaction(Transaction transaction){
        boolean stored = processingService.process(transaction);
        System.out.println("Transaction " + (stored ? "persisted" : "rejected") + ": " + transaction);
    }
}
