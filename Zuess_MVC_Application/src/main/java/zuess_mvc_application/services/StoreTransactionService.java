package zuess_mvc_application.services;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import zuess_mvc_application.domain.*;
import zuess_mvc_application.repository.*;

@Service
public class StoreTransactionService {
	
	@Autowired
	StoreTransactionRepository storeTransactionRepository;
	
	@Autowired
	UserRepository userRepository;

	public StoreTransaction submitNewOrder(List<InventoryItem> cartItemsList, int scholarship_funds_used, String user_email) {
		User user = userRepository.findByEmail(user_email);
		int transaction_total = 0;
		String items_list = "";
		Date transaction_date = new Date(System.currentTimeMillis());
		
		for (InventoryItem item : cartItemsList) {
			transaction_total += item.getPrice();
			items_list += item.getName() + ", ";
		}
		items_list = items_list.substring(0, items_list.length()-2);
		StoreTransaction transaction = new StoreTransaction(user.getId(), transaction_total, scholarship_funds_used, items_list, transaction_date);
		storeTransactionRepository.save(transaction);
		
		return transaction;
	}
	
	public List<StoreTransaction> getTransactionsHistoryByUserId(String user_email, int number_of_transactions){
		User user = userRepository.findByEmail(user_email);
		List<StoreTransaction> transactionsList = storeTransactionRepository.getMostRecentTransactionsByUserID(user.getId(), number_of_transactions);
		return transactionsList;
	}
}
