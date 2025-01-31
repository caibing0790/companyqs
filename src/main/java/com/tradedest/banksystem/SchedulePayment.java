package com.tradedest.banksystem;

public class SchedulePayment {
    private final long executeTime;
    private final String fromAccountId;
    private final String toAccountId;
    private final Long amount;

    public SchedulePayment(long executeTime, String fromAccountId, String toAccountId, Long amount) {
        this.executeTime = executeTime;
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
    }

    public long getExecuteTime() {
        return executeTime;
    }

    public String getFromAccountId() {
        return fromAccountId;
    }

    public String getToAccountId() {
        return toAccountId;
    }

    public Long getAmount() {
        return amount;
    }
}
