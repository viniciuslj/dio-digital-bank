package br.com.digitalbank;

import br.com.digitalbank.account.CurrentAccount;
import br.com.digitalbank.account.IAccount;
import br.com.digitalbank.account.SavingsAccount;
import br.com.digitalbank.account.exception.InsufficientAccountBalanceException;
import br.com.digitalbank.exception.AccountNotFoundException;
import lombok.Getter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class Bank {
    @Getter
    private String name;

    private Map<Integer, IAccount> accounts;

    public Bank(String name) {
        this.name = name;

        accounts = new HashMap<>();
    }

    private IAccount insertAccount(IAccount account) {
        accounts.put(Integer.valueOf(account.getNumber()), account);
        return account;
    }

    private IAccount findAccount(int accountNumber) throws AccountNotFoundException {
        IAccount account = accounts.get(Integer.valueOf(accountNumber));
        if (account == null)
            throw  new AccountNotFoundException();

        return account;
    }

    public int createCurrentAccount(String clientName) {
        return insertAccount(new CurrentAccount(clientName)).getNumber();
    }

    public int createSavingsAccount(String clientName) {
        return insertAccount(new SavingsAccount(clientName)).getNumber();
    }

    public void getCash(int accountNumber, BigDecimal value)
            throws AccountNotFoundException, InsufficientAccountBalanceException{
        findAccount(accountNumber).debit(value);
    }

    public void depositCash(int accountNumber, BigDecimal value)
            throws AccountNotFoundException {
        findAccount(accountNumber).credit(value);
    }

    public void transferCash(BigDecimal value, int fromAccountNumber, int toAccountNumber)
            throws AccountNotFoundException, InsufficientAccountBalanceException {
        IAccount from = findAccount(fromAccountNumber);
        IAccount to = findAccount(toAccountNumber);

        from.debit(value);
        to.credit(value);
    }

    public BigDecimal getAccountBalance(int accountNumber) throws AccountNotFoundException {
        return findAccount(accountNumber).getBalance();
    }

    public String getAccountStatement(int accountNumber, LocalDate from)
            throws AccountNotFoundException {
        return findAccount(accountNumber).getStatement(from);
    }

    public BigDecimal getBalance() {
        return accounts.values().stream()
                .map(account -> account.getBalance())
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, BigDecimal.ROUND_UP);
    }

    public String getStatement() {
        StringBuffer statement = new StringBuffer(getName());
        statement.append("\n").append("Bank Balance: ").append(getBalance());
        statement.append("\n").append("Account\t\tBalance");
        accounts.values().stream()
                .sorted(Comparator.comparing(IAccount::getBalance))
                .forEach(account -> statement.append("\n")
                        .append(account.getNumber()).append("\t\t\t")
                        .append(account.getBalance().setScale(2, BigDecimal.ROUND_UP)));

        return statement.toString();
    }
}
