package com.tradedest.banksystem;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class BankSystem {
    private final Map<String, Long> accounts;
    private final List<Transaction> transactions;
    private final PriorityQueue<SchedulePayment> schedulePayments;

    public BankSystem() {
        this.accounts = new ConcurrentHashMap<>();
        this.transactions = new CopyOnWriteArrayList<>();
        schedulePayments = new PriorityQueue<>((o1, o2) -> {
            if (o1.getExecuteTime() == o2.getExecuteTime()) {
                return o1.getFromAccountId().compareTo(o2.getFromAccountId());
            } else {
                return Long.compare(o1.getExecuteTime(), o2.getExecuteTime());
            }
        });
    }

    public void createAccount(long timestamp, String accountId) {
        accounts.put(accountId, 0L);
    }

    public synchronized long depositMoney(long timestamp, String accountId, Long amount) {
        validateAccount(accountId);
        long balance = accounts.get(accountId) + amount;
        accounts.put(accountId, balance);
        transactions.add(new Transaction(timestamp, accountId, accountId, "Deposit", amount));
        return balance;
    }

    public synchronized void transferMoney(long timestamp, String fromAccountId, String toAccountId, Long amount) {
        validateAccount(fromAccountId);
        validateAccount(toAccountId);
        if (amount > accounts.get(fromAccountId)) {
            throw new IllegalArgumentException("Insufficient balance");
        }
        long fromAccountBalance = accounts.get(fromAccountId) - amount;
        long toAccountBalance = accounts.get(toAccountId) + amount;
        accounts.put(fromAccountId, fromAccountBalance);
        accounts.put(toAccountId, toAccountBalance);
        transactions.add(new Transaction(timestamp, fromAccountId, toAccountId, "Transfer", amount));
    }

    public synchronized void withdrawMoney(long timestamp, String accountId, Long amount) {
        validateAccount(accountId);
        if (amount > accounts.get(accountId)) {
            throw new IllegalArgumentException("Insufficient balance");
        }
        long balance = accounts.get(accountId) - amount;
        accounts.put(accountId, balance);
        transactions.add(new Transaction(timestamp, accountId, accountId, "Withdraw", amount));
    }

    public List<Map.Entry<String, Long>> topKSpendingAccounts(int topK) {
        if (topK <= 0) {
            throw new IllegalArgumentException("Invalid value of K");
        }
        Map<String, Long> statsAccounts = getStatsAccounts();
        List<Map.Entry<String, Long>> sortedStatsAccounts = new ArrayList<>(statsAccounts.entrySet());
        sortedStatsAccounts
                .sort((o1, o2) -> {
                    if (o1.getValue().equals(o2.getValue())) {
                        return o1.getKey().compareTo(o2.getKey());
                    } else {
                        return o2.getValue().compareTo(o1.getValue());
                    }
                });
        return sortedStatsAccounts.subList(0, Math.min(topK, sortedStatsAccounts.size()));
    }

    private Map<String, Long> getStatsAccounts() {
        Map<String, Long> statsAccounts = new HashMap<>();

        for (Transaction transaction : transactions) {
            if (transaction.getTransactionType().equals("Transfer")) {
                statsAccounts.put(transaction.getFromAccountId(), statsAccounts.getOrDefault(transaction.getFromAccountId(), 0L) + transaction.getAmount());
                statsAccounts.put(transaction.getToAccountId(), statsAccounts.getOrDefault(transaction.getToAccountId(), 0L) - transaction.getAmount());
            } else if (transaction.getTransactionType().equals("Withdraw")) {
                statsAccounts.put(transaction.getFromAccountId(), statsAccounts.getOrDefault(transaction.getFromAccountId(), 0L) - transaction.getAmount());
            } else if (transaction.getTransactionType().equals("Deposit")) {
                statsAccounts.put(transaction.getFromAccountId(), statsAccounts.getOrDefault(transaction.getFromAccountId(), 0L) + transaction.getAmount());
            }
        }

        return statsAccounts;
    }

    public Map<String, Long> getAccounts() {
        return Collections.unmodifiableMap(accounts);
    }

    private void validateAccount(String accountId) {
        if (!accounts.containsKey(accountId)) {
            throw new IllegalArgumentException("Account does not exist");
        }
    }

    public void schedulePayment(long timestamp, String fromAccountId, String toAccountId, Long amount, Long timeDelay) {
        validateAccount(fromAccountId);
        long executeTime = timestamp + timeDelay;
        schedulePayments.add(new SchedulePayment(executeTime, fromAccountId, toAccountId, amount));
    }

    public void executeScheduledPayments(long timestamp) {
        while (!schedulePayments.isEmpty() && schedulePayments.peek().getExecuteTime() <= timestamp) {
            SchedulePayment schedulePayment = schedulePayments.poll();
            transferMoney(timestamp, schedulePayment.getFromAccountId(), schedulePayment.getToAccountId(), schedulePayment.getAmount());
        }
    }
}
