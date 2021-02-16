package com.bank.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bank.dao.UserDAO;
import com.bank.model.Recipient;
import com.bank.model.User;
import com.bank.request.RecipientForm;
import com.bank.request.TransactionRequest;
import com.bank.request.TransferRequest;
import com.bank.response.TransactionResponse;
import com.bank.service.AccountService;
import com.bank.service.UserService;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/account")
public class AccountController {

	@Autowired
	AccountService accountService;

	@Autowired
	UserService userService;
	
	@PostMapping("/deposit")
	private ResponseEntity<TransactionResponse> deposit(@Valid @RequestBody TransactionRequest request) {
		TransactionResponse response = new TransactionResponse();	
		User user = (User) SecurityContextHolder.
												getContext().
												getAuthentication().
												getPrincipal();
		accountService.deposit(request, user);
		response.setMessage("Ammount has been succesfully deposited");
		response.setSuccess(true);
		UserDAO userDao = userService.getUserDAOByName(user.getUsername());
		response.setUser(userDao);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PostMapping("/withdraw")
	private ResponseEntity<TransactionResponse> withdraw(@Valid @RequestBody TransactionRequest request) {
		TransactionResponse response = new TransactionResponse();	
		User user = (User)SecurityContextHolder.
												getContext().
												getAuthentication().
												getPrincipal();
		// Account ballance check
		if(user != null && user.getAccount() != null && user.getAccount().getAccountBalance().intValue()>=request.getAmount().intValue()) {
			accountService.withdraw(request, user);
			response.setMessage("Whitdrawal is completed successfully");
			response.setSuccess(true);
			UserDAO userDao = userService.getUserDAOByName(user.getUsername());
			response.setUser(userDao);
		} else {
			response.setMessage("Account balance is not sufficiente");
			response.setSuccess(false);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PostMapping("/addRecipient")
	public ResponseEntity<TransactionResponse> addRecipient(
			@Valid @RequestBody RecipientForm request) {
		TransactionResponse response = new TransactionResponse();
		User user = (User) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		Recipient recipient = new Recipient(request.getName(),
				request.getEmail(), request.getPhone(), request.getBankName(),
				request.getBankNumber());
		recipient.setUser(user);
		accountService.saveRecipient(recipient);
		response.setMessage("Recipient added successfully");
		response.setSuccess(true);
		UserDAO userDAO = userService.getUserDAOByName(user.getUsername());
		response.setUser(userDAO);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PostMapping("/transfer")
	public ResponseEntity<TransactionResponse> transfer(
			@Valid @RequestBody TransferRequest transferRequest) {
		TransactionResponse response = new TransactionResponse();
		User user = (User) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		
		if(user != null && user.getAccount() != null && user.getAccount().getAccountBalance().intValue()>=transferRequest.getAmount().intValue()) {
			accountService.transfer(transferRequest, user);
			response.setMessage("Transfer is completed successfully");
			response.setSuccess(true);
			UserDAO userDao = userService.getUserDAOByName(user.getUsername());
			response.setUser(userDao);
		} else {
			response.setMessage("Sorry, balance is not sufficiente");
			response.setSuccess(false);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}