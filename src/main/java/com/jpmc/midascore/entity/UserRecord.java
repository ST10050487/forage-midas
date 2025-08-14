package com.jpmc.midascore.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(name = "uk_users_name", columnNames = "name"))
public class UserRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Remove this if IDs must match external source exactly.
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal balance = BigDecimal.ZERO;

    @Version
    private Long version;

    protected UserRecord() {
    }

    public UserRecord(String name, BigDecimal balance) {
        this.name = name;
        this.balance = balance == null ? BigDecimal.ZERO : balance;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public BigDecimal getBalance() { return balance; }

    public void debit(BigDecimal amount) {
        if (amount.signum() < 0) throw new IllegalArgumentException("Negative debit");
        this.balance = this.balance.subtract(amount);
    }

    public void credit(BigDecimal amount) {
        if (amount.signum() < 0) throw new IllegalArgumentException("Negative credit");
        this.balance = this.balance.add(amount);
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "UserRecord{id=" + id + ", name='" + name + "', balance=" + balance + "}";
    }

    public UserRecord(String name, float balance) {
        this(name, BigDecimal.valueOf(balance));
    }
}