package br.com.digitalbank.account;

import br.com.digitalbank.account.exception.InsufficientAccountBalanceException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface IAccount {
    int getNumber();

    String getName();

    BigDecimal getBalance();

    List<Map.Entry<LocalDate, BigDecimal>> getTransactions();

    void credit(BigDecimal value, LocalDate date);
    void credit(BigDecimal value);

    void debit(BigDecimal value, LocalDate date) throws InsufficientAccountBalanceException;
    void debit(BigDecimal value) throws InsufficientAccountBalanceException;

    String getStatement(LocalDate from);
}
