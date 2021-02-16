package com.bank.repository;

import org.springframework.data.repository.CrudRepository;

import com.bank.model.Transaction;

public interface TransactionRepo extends CrudRepository<Transaction, Long>{

}
