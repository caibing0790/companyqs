package com.tradedest.banksystem;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;
import java.util.AbstractMap;

public class BankSystemTest {

    @Test
    public void testBasicOperations() {
        BankSystem bank = new BankSystem();
        bank.createAccount(0, "alice");
        bank.createAccount(0, "bob");
        assertEquals(100, bank.depositMoney(1, "alice", 100L));
        bank.transferMoney(2, "alice", "bob", 50L);
        assertEquals(50, bank.getAccounts().get("alice"));
        assertEquals(50, bank.getAccounts().get("bob"));
    }

    @Test
    public void testWithdrawAndTopK() {
        BankSystem bank = new BankSystem();
        bank.createAccount(0, "alice");
        bank.depositMoney(1, "alice", 200L);
        bank.withdrawMoney(2, "alice", 100L);
        List<Map.Entry<String, Long>> topSpenders = bank.topKSpendingAccounts(1);
        assertEquals(new AbstractMap.SimpleEntry<>("alice", 100L), topSpenders.get(0));
    }

    @Test
    public void testSchedulePayment() {
        BankSystem bank = new BankSystem();
        bank.createAccount(0, "alice");
        bank.createAccount(0, "caibing");
        bank.depositMoney(1, "alice", 200L);
        bank.schedulePayment(2, "alice", "caibing", 150L, 10L);
        bank.executeScheduledPayments(13);
        assertEquals(50, bank.getAccounts().get("alice").intValue());
    }
}