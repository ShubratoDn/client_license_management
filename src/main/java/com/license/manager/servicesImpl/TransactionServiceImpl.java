package com.license.manager.servicesImpl;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.license.manager.DTO.LicenseDTO;
import com.license.manager.DTO.TransactionDTO;
import com.license.manager.DTO.TransactionHistoryDto;
import com.license.manager.DTO.UserDTO;
import com.license.manager.config.Constants;
import com.license.manager.entities.License;
import com.license.manager.entities.Transaction;
import com.license.manager.entities.User;
import com.license.manager.exceptions.ResourceNotFoundException;
import com.license.manager.payloads.PageableResponse;
import com.license.manager.repositories.TransactionRepository;
import com.license.manager.services.TransactionService;

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




	@Override
	public PageableResponse getAllTransationPageable(int pageNumber, int pageSize, String sortBy,
			String sortDirection) {
	
		Sort sort = null;
		if (sortDirection.equalsIgnoreCase("asc")) {
			sort = Sort.by(sortBy).ascending();
		} else {
			sort = Sort.by(sortBy).descending();
		}

		
		Page<Transaction> pageInfo;
		try {
			Pageable page = PageRequest.of(pageNumber, pageSize, sort);
			pageInfo = transactionRepository.findAll(page);
		} catch (Exception e) {
			Pageable page = PageRequest.of(pageNumber, pageSize, Sort.by("transactionId").descending());
			pageInfo = transactionRepository.findAll(page);
		}
		
		
		List<Transaction> content = pageInfo.getContent();
		
		// Convert the list of Product entities to ProductDTOs
		List<TransactionDTO> transactionDTOs = content.stream().map(transaction -> modelMapper.map(transaction, TransactionDTO.class))
				.collect(Collectors.toList());
		

		PageableResponse pageData = new PageableResponse();
		pageData.setContent(transactionDTOs);
		pageData.setPageNumber(pageInfo.getNumber());
		pageData.setPageSize(pageInfo.getSize());
		pageData.setTotalElements(pageInfo.getTotalElements());
		pageData.setTotalPages(pageInfo.getTotalPages());
		pageData.setNumberOfElements(pageInfo.getNumberOfElements());

		pageData.setEmpty(pageInfo.isEmpty());
		pageData.setFirst(pageInfo.isFirst());
		pageData.setLast(pageInfo.isLast());
		

		
		return pageData;
	}



	//get transaction by ID
	@Override
	public TransactionDTO getTransactionById(long transactionId) {
		Transaction transaction = transactionRepository.findById(transactionId).orElseThrow(()-> new ResourceNotFoundException("Transaction", transactionId+""));
		TransactionDTO map = modelMapper.map(transaction, TransactionDTO.class);
		return map;
	}
	
	
	
	//delete transaction By ID	
	@Override
	public void deleteTransactionById(long transactionId) {
		transactionRepository.deleteById(transactionId);		
	}




	@Override
	public TransactionDTO updateTransactionById(long transactionId, TransactionDTO newTransaction) {
		TransactionDTO oldTransaction = this.getTransactionById(transactionId);
		
		oldTransaction.setAmount(newTransaction.getAmount());
		oldTransaction.setLicense(newTransaction.getLicense());
		oldTransaction.setPaymentStatus(newTransaction.getPaymentStatus());
		oldTransaction.setTransactionDate(newTransaction.getTransactionDate());
		oldTransaction.setTransactionType(newTransaction.getTransactionType());
		oldTransaction.setUser(newTransaction.getUser());
		
		Transaction updateTransaction = modelMapper.map(oldTransaction, Transaction.class);
		Transaction save = transactionRepository.save(updateTransaction);
		TransactionDTO map = modelMapper.map(save, TransactionDTO.class);
		return map;
	}
	
	

}
