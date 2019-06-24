package dev.rouchy.roundUp.models;

public enum TransactionSource {
    CASH_DEPOSIT,
    CASH_DEPOSIT_CHARGE,
    CASH_WITHDRAWAL,
    CASH_WITHDRAWAL_CHARGE,
    CHAPS,
    CHEQUE,
    CURRENCY_CLOUD,
    DIRECT_CREDIT,
    DIRECT_DEBIT,
    DIRECT_DEBIT_DISPUTE,
    INTERNAL_TRANSFER,
    MASTER_CARD,
    MASTERCARD_MONEYSEND,
    MASTERCARD_CHARGEBACK,
    FASTER_PAYMENTS_IN,
    FASTER_PAYMENTS_OUT,
    FASTER_PAYMENTS_REVERSAL,
    STRIPE_FUNDING,
    INTEREST_PAYMENT,
    NOSTRO_DEPOSIT,
    OVERDRAFT,
    OVERDRAFT_INTEREST_WAIVED,
    FASTER_PAYMENTS_REFUND,
    STARLING_PAY_STRIPE,
    ON_US_PAY_ME,
    LOAN_PRINCIPAL_PAYMENT,
    LOAN_REPAYMENT,
    LOAN_OVERPAYMENT,
    LOAN_LATE_PAYMENT,
    SEPA_CREDIT_TRANSFER,
    SEPA_DIRECT_DEBIT,
    TARGET2_CUSTOMER_PAYMENT,
    FX_TRANSFER,
    ISS_PAYMENT,
    STARLING_PAYMENT
}