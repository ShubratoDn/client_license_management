package com.license.manager.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.license.manager.entities.Transaction;
import com.license.manager.entities.User;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

	List<Transaction> findByUser(User user);
	
}
