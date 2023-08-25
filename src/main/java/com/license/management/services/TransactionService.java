package com.license.management.services;

import java.util.List;

import com.license.management.DTO.LicenseDTO;
import com.license.management.DTO.TransactionDTO;
import com.license.management.DTO.TransactionHistoryDto;
import com.license.management.DTO.UserDTO;

public interface TransactionService {
	public TransactionDTO addTransaction(UserDTO userDTO, LicenseDTO licenseDTO);
	
	public List<TransactionHistoryDto> getMyAllTransactions(UserDTO userDTO);
}
