package com.banksystem.connectbanksystemdb.controller;

public class CreateAccountRequest {
    private String accountNumber;
    private double initialBalance;

    public String getAccountNumber(){
        return accountNumber;
    }
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public double getInitialBalance(){
        return initialBalance;
    }
    public void setInitialBalance(double initialBalance){
        this.initialBalance = initialBalance;
    }
}
