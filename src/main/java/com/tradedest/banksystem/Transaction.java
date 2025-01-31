package com.tradedest.banksystem;

public class Transaction {
    private final long timestamp;
    private final String fromAccountId;
    private final String toAccountId;
    private final String transactionType;
    private final Long amount;

    public Transaction(long timestamp, String fromAccountId, String toAccountId, String transactionType, Long amount) {
        this.timestamp = timestamp;
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.transactionType = transactionType;
        this.amount = amount;
    }

    public String getFromAccountId() {
        return fromAccountId;
    }

    public String getToAccountId() {
        return toAccountId;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public Long getAmount() {
        return amount;
    }
}
