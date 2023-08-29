package com.license.manager.services;

import java.util.List;

import com.license.manager.DTO.LicenseDTO;
import com.license.manager.DTO.TransactionDTO;
import com.license.manager.DTO.TransactionHistoryDto;
import com.license.manager.DTO.UserDTO;
import com.license.manager.payloads.PageableResponse;

public interface TransactionService {
	public TransactionDTO addTransaction(UserDTO userDTO, LicenseDTO licenseDTO);
	
	public List<TransactionHistoryDto> getMyAllTransactions(UserDTO userDTO);
	
	public PageableResponse getAllTransationPageable(int pageNumber, int pageSize, String sortBy, String sortDirection);
	
	public TransactionDTO getTransactionById(long transactionId);
	
	public void deleteTransactionById(long transactionId);
	
	public TransactionDTO updateTransactionById(long transactionId, TransactionDTO transactionDTO);
}
