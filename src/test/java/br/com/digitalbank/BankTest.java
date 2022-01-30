package br.com.digitalbank;

import br.com.digitalbank.account.CurrentAccount;
import br.com.digitalbank.account.exception.InsufficientAccountBalanceException;
import br.com.digitalbank.exception.AccountNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

public class BankTest {
    Bank bank;
    int accountRogerWaters;
    int accountDavidGilmour;
    int accountNickMason;

    @BeforeEach
    void setUp() {
        bank = new Bank("Pink Floyd Bank");
        accountRogerWaters = bank.createCurrentAccount("Roger Waters");
        accountDavidGilmour = bank.createCurrentAccount("David Gilmour");
        accountNickMason = bank.createSavingsAccount("Nick Mason");
    }

    @Test
    void overdraftProtectionException() throws AccountNotFoundException {
        assertThrows(InsufficientAccountBalanceException.class, () ->
                bank.getCash(accountRogerWaters, CurrentAccount.OVERDRAFT_PROTECTION.add(BigDecimal.valueOf(10))));

        assertThrows(InsufficientAccountBalanceException.class, () ->
                bank.transferCash(CurrentAccount.OVERDRAFT_PROTECTION.add(BigDecimal.valueOf(0.1)),
                accountRogerWaters, accountDavidGilmour));

        assertEquals(bank.getAccountBalance(accountRogerWaters).doubleValue(), 0.0);
        assertEquals(bank.getAccountBalance(accountDavidGilmour).doubleValue(), 0.0);
    }

    @Test
    void overdraftProtection() throws AccountNotFoundException, InsufficientAccountBalanceException {
        bank.transferCash(CurrentAccount.OVERDRAFT_PROTECTION.subtract(BigDecimal.valueOf(0.1)),
                accountRogerWaters, accountDavidGilmour);

        assertEquals(CurrentAccount.OVERDRAFT_PROTECTION.subtract(BigDecimal.valueOf(0.1)).negate().doubleValue(),
                bank.getAccountBalance(accountRogerWaters).doubleValue());

        assertEquals(CurrentAccount.OVERDRAFT_PROTECTION.subtract(BigDecimal.valueOf(0.1)).doubleValue(),
                bank.getAccountBalance(accountDavidGilmour).doubleValue());
    }

    @Test
    void bankBalance() throws AccountNotFoundException, InsufficientAccountBalanceException {
        bank.depositCash(accountRogerWaters, BigDecimal.valueOf(1000.99));
        bank.depositCash(accountDavidGilmour, BigDecimal.valueOf(100));
        bank.transferCash(BigDecimal.valueOf(900), accountRogerWaters, accountNickMason);

        assertEquals(1100.99, bank.getBalance().doubleValue());
    }

    @Test
    void bankStatement() throws AccountNotFoundException, InsufficientAccountBalanceException {
        bank.depositCash(accountRogerWaters, BigDecimal.valueOf(1000.99));
        bank.depositCash(accountDavidGilmour, BigDecimal.valueOf(100));
        bank.transferCash(BigDecimal.valueOf(900), accountRogerWaters, accountNickMason);

        String result = bank.getStatement();

        assertEquals(6, result.split("\n").length);
    }
}
