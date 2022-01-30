package br.com.digitalbank.account;

import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.concurrent.atomic.DoubleAccumulator;

@AllArgsConstructor
public class AccountStatement {
    IAccount account;
    LocalDate from;

    public String generate() {
        StringBuffer statement = new StringBuffer();
        BigDecimal previousBalance = reducePreviousBalance();
        statement
                .append("Name   :\t\t").append(account.getName())
                .append("\nAccount:\t\t").append(account.getNumber())
                .append("\nBalance:\t\t").append(applyPrecision(account.getBalance()))
                .append("\nTransactions from: ").append(from.toString())
                .append("\n").append(from).append("\t\t").append(applyPrecision(previousBalance))
                .append(generateTransactionSection(previousBalance));

        return statement.toString();
    }

    private BigDecimal reducePreviousBalance() {
        return account.getTransactions().stream()
                .filter(t -> t.getKey().isBefore(from))
                .map(t -> t.getValue())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private StringBuffer generateTransactionSection(BigDecimal initialValue) {
        StringBuffer transactions = new StringBuffer();
        DoubleAccumulator accumulator = new DoubleAccumulator(Double::sum, initialValue.doubleValue());
        account.getTransactions().stream()
                .filter(t -> !t.getKey().isBefore(from))
                .forEach(t -> {
                    accumulator.accumulate(t.getValue().doubleValue());
                    transactions
                            .append("\n")
                            .append(t.getKey()) // Date
                            .append("\t\t")
                            .append(t.getValue().signum() < 0 ? "" : "+") // Signal
                            .append(applyPrecision(t.getValue())) // Value
                            .append("\nBalance:\t\t").append(applyPrecision(BigDecimal.valueOf(accumulator.doubleValue())));
                });

        return transactions;
    }

    private static BigDecimal applyPrecision(BigDecimal value) {
        return value.setScale(2, BigDecimal.ROUND_UP);
    }
}
