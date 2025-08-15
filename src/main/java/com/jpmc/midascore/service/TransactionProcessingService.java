package com.jpmc.midascore.service;

        import com.jpmc.midascore.foundation.Transaction;
        import com.jpmc.midascore.foundation.Incentive;
        import com.jpmc.midascore.entity.UserRecord;
        import com.jpmc.midascore.domain.TransactionRecord;
        import com.jpmc.midascore.repository.UserRepository;
        import com.jpmc.midascore.repository.TransactionRecordRepository;
        import org.springframework.stereotype.Service;
        import org.springframework.transaction.annotation.Transactional;
        import org.springframework.web.client.RestTemplate;
        import java.math.BigDecimal;
        import java.util.Optional;

        @Service
        public class TransactionProcessingService {

            private final UserRepository userRepository;
            private final TransactionRecordRepository transactionalRepository;
            private final RestTemplate restTemplate;

            private static final String INCENTIVE_URL = "http://localhost:8080/incentive";

            public TransactionProcessingService(UserRepository userRepository,
                                                TransactionRecordRepository transactionalRepository,
                                                RestTemplate restTemplate) {
                this.userRepository = userRepository;
                this.transactionalRepository = transactionalRepository;
                this.restTemplate = restTemplate;
            }

            @Transactional
            public boolean process(Transaction incoming) {
                Long senderId = incoming.getSenderId();
                Long recipientId = incoming.getRecipientId();
                BigDecimal amount = BigDecimal.valueOf(incoming.getAmount());

                Optional<UserRecord> senderOpt = userRepository.findById(senderId);
                Optional<UserRecord> recipientOpt = userRepository.findById(recipientId);
                if (senderOpt.isEmpty() || recipientOpt.isEmpty()) return false;

                UserRecord sender = senderOpt.get();
                UserRecord recipient = recipientOpt.get();
                if (sender.getBalance().compareTo(amount) < 0) return false;

                BigDecimal incentiveAmt = fetchIncentive(incoming);

                sender.debit(amount);
                recipient.credit(amount.add(incentiveAmt));

                transactionalRepository.save(
                        new TransactionRecord(sender, recipient, amount, incentiveAmt)
                );
                return true;
            }

            private BigDecimal fetchIncentive(Transaction tx) {
                try {
                    Incentive incentive = restTemplate.postForObject(INCENTIVE_URL, tx, Incentive.class);
                    if (incentive == null) return BigDecimal.ZERO;
                    float raw = Math.max(0f, incentive.getAmount());
                    return BigDecimal.valueOf(raw);
                } catch (Exception e) {
                    return BigDecimal.ZERO;
                }
            }
        }