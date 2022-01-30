package br.com.digitalbank.account;

import br.com.digitalbank.account.exception.InsufficientAccountBalanceException;
import lombok.Getter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.AbstractMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class Account implements IAccount {
    private static int AUTO_INCREMENT_NUMBER = 1;

    @Getter
    protected int number;

    @Getter
    protected String name;

    @Getter
    protected BigDecimal balance;

    @Getter
    protected List<Map.Entry<LocalDate, BigDecimal>> transactions;

    public Account(String name) {
        this.name = name;
        number = AUTO_INCREMENT_NUMBER++;
        balance = BigDecimal.ZERO;
        transactions = new LinkedList<>();
    }

    @Override
    public void credit(BigDecimal value, LocalDate date) {
        balance = balance.add(value);
        transactions.add(new AbstractMap.SimpleImmutableEntry<>(date, value));
    }

    @Override
    public void credit(BigDecimal value) {
        credit(value, LocalDate.now());
    }

    @Override
    public void debit(BigDecimal value, LocalDate date) throws InsufficientAccountBalanceException {
        balance = balance.subtract(value);
        transactions.add(new AbstractMap.SimpleImmutableEntry<>(date, value.negate()));
    }

    @Override
    public void debit(BigDecimal value) throws InsufficientAccountBalanceException {
        debit(value, LocalDate.now());
    }

    @Override
    public String getStatement(LocalDate from) {
        return new AccountStatement(this, from).generate();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Account account = (Account) o;

        return getNumber() == account.getNumber();
    }

    @Override
    public int hashCode() {
        return getNumber();
    }
}
