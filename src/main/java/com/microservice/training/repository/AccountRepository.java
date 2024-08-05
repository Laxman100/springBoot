package com.microservice.training.repository;

import org.springframework.data.repository.CrudRepository;

import com.microservice.training.bean.Account;

import java.util.Optional;

public interface AccountRepository extends CrudRepository<Account, Integer> {

  Optional<Account> findByAccountId(int accountId);
}
