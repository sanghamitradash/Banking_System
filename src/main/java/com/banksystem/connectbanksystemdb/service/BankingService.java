package com.banksystem.connectbanksystemdb.service;

import com.banksystem.connectbanksystemdb.model.Account;
import com.banksystem.connectbanksystemdb.repository.AccountRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class BankingService {

    @Autowired
    private AccountRepository accountRepository;

    @Transactional
    public void createAccount(String accountNumber, double initialBalance) throws IOException{
        Account newAccount = new Account();
        newAccount.setAccountNumber(accountNumber);
        newAccount.setBalance(initialBalance);

        accountRepository.save(newAccount);
    }

    @Transactional
    public void performTransaction(String accountNumber, double amount, TransactionType type) throws IOException{
        Account account = accountRepository.findByAccountNumber(accountNumber);

        if(account == null){
            throw new IllegalArgumentException("Account not found");
        }

        if(type == TransactionType.DEPOSIT){
            account.setBalance(account.getBalance() + amount);
        }
        else if(type == TransactionType.WITHDRAW){
            if(account.getBalance() >= amount){
                account.setBalance(account.getBalance() - amount);
            }
            else{
                throw new IllegalArgumentException("Insufficient Balance");
            }
        }
        accountRepository.save(account);
    }

    public double checkBalance(String accountNumber){
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if(account == null){
            throw new IllegalArgumentException("Account not found");
        }
        return account.getBalance();
    }
}
