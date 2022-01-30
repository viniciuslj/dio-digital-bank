package br.com.digitalbank.account;

import br.com.digitalbank.account.exception.InsufficientAccountBalanceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.math.BigDecimal;
import java.time.LocalDate;

public class AccountStatementTest {
    IAccount account;

    @BeforeEach
    void setUp() throws InsufficientAccountBalanceException {
        account = new CurrentAccount("Roger Waters");

        account.credit(BigDecimal.valueOf(100), LocalDate.now().minusDays(30));
        account.credit(BigDecimal.valueOf(99.50), LocalDate.now().minusDays(25));
        account.credit(BigDecimal.valueOf(10.35), LocalDate.now().minusDays(22));
        account.debit(BigDecimal.valueOf(0.35), LocalDate.now().minusDays(22));
        account.credit(BigDecimal.valueOf(1), LocalDate.now().minusDays(10));
        account.credit(BigDecimal.valueOf(1000.89), LocalDate.now().minusDays(19));
        account.debit(BigDecimal.valueOf(100.89), LocalDate.now().minusDays(5));
        account.credit(BigDecimal.valueOf(50), LocalDate.now().minusDays(2));
        account.debit(BigDecimal.valueOf(15.50), LocalDate.now().minusDays(1));
        account.debit(BigDecimal.valueOf(100), LocalDate.now());
    }

    @Test
    void allTransactions() {
        AccountStatement statement = new AccountStatement(account, LocalDate.now().minusDays(31));
        String result = statement.generate();

        assertEquals(25, result.split("\n").length);
    }

    @Test
    void todayTransactions() {
        AccountStatement statement = new AccountStatement(account, LocalDate.now());
        String result = statement.generate();

        assertEquals(7, result.split("\n").length);
    }

    @Test
    void fromTwoDaysAgoTransactions() {
        AccountStatement statement = new AccountStatement(account, LocalDate.now().minusDays(2));
        String result = statement.generate();

        assertEquals(11, result.split("\n").length);
    }
}
