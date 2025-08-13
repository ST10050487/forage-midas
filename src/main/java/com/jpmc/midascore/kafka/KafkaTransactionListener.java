package com.jpmc.midascore.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import com.jpmc.midascore.foundation.Transaction;

@Component
public class KafkaTransactionListener {
    //Using topic from configuration: general.kafka-topic
    @KafkaListener(topics = "${general.kafka-topic}",groupId = "midas-core")
    public void onTransaction(@Payload Transaction transaction){
        //Logging the transaction received
        System.out.println("Transaction received: " + transaction);
        //Process the transaction (e.g., save to database, perform business logic, etc.)
        //This is a placeholder for actual processing logic
        //processTransaction(transaction);
    }
}
