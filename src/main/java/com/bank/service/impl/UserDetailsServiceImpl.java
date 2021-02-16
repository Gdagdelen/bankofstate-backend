package com.bank.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import static com.bank.util.DateUtil.getDateAsString;
import static com.bank.util.DateUtil.SIMPLE_DATE_TIME_FORMAT;
import static com.bank.util.DateUtil.SIMPLE_DATE_FORMAT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.bank.dao.RecipientDAO;
import com.bank.dao.TransactionDAO;
import com.bank.dao.UserDAO;
import com.bank.model.Recipient;
import com.bank.model.Transaction;
import com.bank.model.User;
import com.bank.repository.UserRepo;
import com.bank.service.UserService;

@Service
public class UserDetailsServiceImpl implements UserDetailsService, UserService {
	
	@Autowired
	UserRepo userRepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user = userRepo.findByUsername(username).orElseThrow(() -> 
					new UsernameNotFoundException("User Not Found with  username  " + username));
	    return user;			
	}
	
	@Override
	public UserDAO getUserDAO(User user) {
		UserDAO userDAO = new UserDAO();
		userDAO.setUserId(user.getUserId());
		userDAO.setUsername(user.getUsername());
		userDAO.setFirstName(user.getFirstName());
		userDAO.setLastName(user.getLastName());
		userDAO.setEmail(user.getEmail());
		userDAO.setPhone(user.getPhone());
		Boolean isAdmin = user.getUserRoles().stream().
							filter(role -> role.getRole().getName().equals("admin")).
							findAny().isPresent();
		
		userDAO.setIsAdmin(isAdmin);
		
		if(user.getAccount() != null) {
			userDAO.setAccountNumber(user.getAccount().getAccountNumber());
			userDAO.setAccountBalance(user.getAccount().getAccountBalance());			
		}
			// Convert Transaction into TransactionDAO
		List<TransactionDAO> transactions = user.getAccount().getTransactions()
				.stream().map(this::getTransactionDAO)
				.collect(Collectors.toList());
		userDAO.setTransactions(transactions);
		List<RecipientDAO> recipients = user.getRecipients().stream().map(this::getRecipientDAO).collect(Collectors.toList());
		userDAO.setRecipients(recipients);
		return userDAO;
	}

	@Override
	public UserDAO getUserDAOByName(String userName) {
		UserDAO userDAO = null;
		Optional<User> user = userRepo.findByUsername(userName);
		if(user.isPresent()) {
			userDAO = getUserDAO(user.get());
		}
		return userDAO;
	}

	@Override
	public List<UserDAO> getAllUsers() {
		return null;
	}
	
	private TransactionDAO getTransactionDAO(Transaction transaction) {
		TransactionDAO dao = new TransactionDAO();
		dao.setId(transaction.getId());
		dao.setDate(getDateAsString(transaction.getDate(), SIMPLE_DATE_FORMAT));
		dao.setDate(getDateAsString(transaction.getDate(), SIMPLE_DATE_TIME_FORMAT));
		dao.setAmount(transaction.getAmount());
		dao.setAvailableBalance(transaction.getAvailableBalance());
		dao.setDescription(transaction.getDescription());
		dao.setType(transaction.getType());
		dao.setIsTransfer(transaction.getIsTransfer());
		return dao;
	}
	

	private RecipientDAO getRecipientDAO(Recipient recipient) {
		RecipientDAO recipientDAO = new RecipientDAO();
		recipientDAO.setId(recipient.getId());
		recipientDAO.setName(recipient.getName());
		recipientDAO.setEmail(recipient.getEmail());
		recipientDAO.setPhone(recipient.getPhone());
		recipientDAO.setBankName(recipient.getBankName());
		recipientDAO.setBankNumber(recipient.getBankNumber());
		return recipientDAO;
	}

}
