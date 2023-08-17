package com.banksystem.connectbanksystemdb.repository;

import com.banksystem.connectbanksystemdb.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository <Account, Long> {
    Account findByAccountNumber(String AccountNumber);
}
