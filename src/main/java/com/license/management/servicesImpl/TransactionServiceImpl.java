package com.license.management.servicesImpl;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.license.management.DTO.LicenseDTO;
import com.license.management.DTO.TransactionDTO;
import com.license.management.DTO.TransactionHistoryDto;
import com.license.management.DTO.UserDTO;
import com.license.management.config.Constants;
import com.license.management.entities.License;
import com.license.management.entities.Transaction;
import com.license.management.entities.User;
import com.license.management.repositories.TransactionRepository;
import com.license.management.services.TransactionService;

@Service
public class TransactionServiceImpl implements TransactionService {

	@Autowired
	private TransactionRepository transactionRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public TransactionDTO addTransaction(UserDTO userDTO, LicenseDTO licenseDTO) {
		
		Transaction transaction = new Transaction();
		transaction.setLicense(modelMapper.map(licenseDTO, License.class));
		transaction.setAmount(licenseDTO.getPrice());
		transaction.setPaymentStatus(Constants.PAYMENT_STATUS_SUCCESSFUL);
		transaction.setTransactionType(Constants.TRANSACTION_TYPE_PURCHASE);
		transaction.setTransactionDate(Timestamp.from(Instant.now()));
		transaction.setUser(modelMapper.map(userDTO, User.class));
		
		Transaction save = transactionRepository.save(transaction);
		
		return modelMapper.map(save, TransactionDTO.class);
	}

	
	
	
	@Override
	public List<TransactionHistoryDto> getMyAllTransactions(UserDTO userDTO) {		
		List<Transaction> transactions = transactionRepository.findByUser(modelMapper.map(userDTO, User.class));		
		List<TransactionHistoryDto> historyDtos = new ArrayList<>();
		
		for(Transaction transaction: transactions) {
			TransactionHistoryDto map = modelMapper.map(transaction, TransactionHistoryDto.class);
			map.setUsername(transaction.getUser().getUsername());
			
			historyDtos.add(map);
		}
		
		return historyDtos;
	}

}
