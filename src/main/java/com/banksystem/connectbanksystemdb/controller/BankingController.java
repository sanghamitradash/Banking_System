package com.banksystem.connectbanksystemdb.controller;

import com.banksystem.connectbanksystemdb.service.BankingService;
import com.banksystem.connectbanksystemdb.service.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/bank")
public class BankingController {
    @Autowired
    private BankingService bankingService;

    @PostMapping("/create")
    public String createAccount(@RequestBody CreateAccountRequest request) {
        try {
            bankingService.createAccount(request.getAccountNumber(), request.getInitialBalance());
            return "Account created successfully";
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/deposit")
    public String deposit(@RequestParam String accountNumber, @RequestParam double amount) {
        try {
            bankingService.performTransaction(accountNumber, amount, TransactionType.DEPOSIT);
            return "Deposited successfully";
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @PostMapping("/withdraw")
    public String withdraw(@RequestParam String accountNumber, @RequestParam double amount){
        try {
            bankingService.performTransaction(accountNumber, amount, TransactionType.WITHDRAW);
            return "Withdrawed successfully";
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/balance")
    public double checkBalance(@RequestParam String accountNumber){
        return bankingService.checkBalance(accountNumber);
    }

}
