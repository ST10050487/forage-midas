package com.jpmc.midascore.service;

import com.jpmc.midascore.foundation.Balance;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class BalanceService {

    private final UserRepository userRepository;

    public BalanceService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Balance getBalance(Long userId) {
        Optional<UserRecord> opt = userRepository.findById(userId);
        BigDecimal amount = opt.map(UserRecord::getBalance).orElse(BigDecimal.ZERO);
        return new Balance(amount.floatValue());
    }
}
