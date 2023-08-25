package com.license.management.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.license.management.entities.Transaction;
import com.license.management.entities.User;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

	List<Transaction> findByUser(User user);
	
}
