package dev.rouchy.roundUp.models;

public enum TransactionStatus {
    UPCOMING,
    PENDING,
    REVERSED,
    SETTLED,
    DECLINED,
    REFUNDED,
    RETRYING,
    ACCOUNT_CHECK
}
