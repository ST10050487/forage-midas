package com.jpmc.midascore.service;

import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.domain.TransactionRecord;
import com.jpmc.midascore.repository.UserRepository;
import com.jpmc.midascore.repository.TransactionRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.Optional;

@Service
public class TransactionProcessingService {

    private final UserRepository userRepository;
    private final TransactionRecordRepository transactionalRepository;

    public TransactionProcessingService(UserRepository userRepository, TransactionRecordRepository transactionalRepository) {
        this.userRepository = userRepository;
        this.transactionalRepository = transactionalRepository;
    }

    @Transactional
    public boolean process(Transaction incoming)
    {
        Long senderId = Long.valueOf(incoming.getSenderId());
        Long recipientId = Long.valueOf(incoming.getRecipientId());
        BigDecimal amount = BigDecimal.valueOf(incoming.getAmount());

        Optional<UserRecord> senderOpt = userRepository.findById(senderId);
        Optional<UserRecord> recipientOpt = userRepository.findById(recipientId);
        if (senderOpt.isEmpty() || recipientOpt.isEmpty()) {
            return false; // User not found
        }
        UserRecord sender = senderOpt.get();
        UserRecord recipient = recipientOpt.get();
        if (sender.getBalance().compareTo(amount) < 0) {
            return false; // Insufficient funds
        }
        sender.debit(amount);
        recipient.credit(amount);

        transactionalRepository.save(new TransactionRecord(sender, recipient, amount));
        return true; // Transaction successful
    }
}
