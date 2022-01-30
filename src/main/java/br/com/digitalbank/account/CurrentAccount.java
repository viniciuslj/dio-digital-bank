package br.com.digitalbank.account;

import br.com.digitalbank.account.exception.InsufficientAccountBalanceException;
import java.math.BigDecimal;
import java.time.LocalDate;

public class CurrentAccount extends Account {
    public static final BigDecimal OVERDRAFT_PROTECTION = new BigDecimal(1000d);

    public CurrentAccount(String name) {
        super(name);
    }

    @Override
    public void debit(BigDecimal value, LocalDate date) throws InsufficientAccountBalanceException {
        if(balance.subtract(value).add(OVERDRAFT_PROTECTION).compareTo(BigDecimal.ZERO) == -1)
            throw new InsufficientAccountBalanceException();

        super.debit(value, date);
    }

    @Override
    public void debit(BigDecimal value) throws InsufficientAccountBalanceException {
        debit(value, LocalDate.now());
    }
}
