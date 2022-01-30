package br.com.digitalbank.account;

import br.com.digitalbank.account.exception.InsufficientAccountBalanceException;
import java.math.BigDecimal;
import java.time.LocalDate;

public class SavingsAccount extends Account {

    public SavingsAccount(String name) {
        super(name);
    }

    @Override
    public void debit(BigDecimal value, LocalDate date) throws InsufficientAccountBalanceException {
        if(balance.subtract(value).compareTo(BigDecimal.ZERO) == -1)
            throw new InsufficientAccountBalanceException();

        super.debit(value, date);
    }

    @Override
    public void debit(BigDecimal value) throws InsufficientAccountBalanceException {
        super.debit(value, LocalDate.now());
    }
}
