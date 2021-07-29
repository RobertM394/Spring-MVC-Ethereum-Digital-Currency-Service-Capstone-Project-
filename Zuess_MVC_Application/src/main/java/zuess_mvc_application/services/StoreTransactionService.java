/**
 * Store Transaction Service contains methods to handle new store transactions purchased with Ethereum account
 * main funds, as well as Scholarship (Ethereum Allowance) funds
 * @author Robert Meis
 */

package zuess_mvc_application.services;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import zuess_mvc_application.domain.*;
import zuess_mvc_application.repository.*;
import zuess_mvc_application.services.*;

@Service
public class StoreTransactionService {
	
	@Autowired
	StoreTransactionRepository storeTransactionRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired 
	BlockchainService blockchainService;

	public StoreTransaction submitNewOrderToDatabase(List<InventoryItem> cartItemsList, int scholarship_funds_used, String user_email) {
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
	
	public Boolean submitNewOrderToEthereum(OtterCoin otterCoin, String storeEthAddress, String userEmail, int orderTotal, List<String> receiptList) throws IOException, InterruptedException, ExecutionException {
		User user = userRepository.findByEmail(userEmail);
		Boolean success = blockchainService.transferUsingCustomFromAddress(otterCoin, user.getEth_account_id(), storeEthAddress, orderTotal, receiptList);
		return success;
	}
	
	public Boolean spendScholarshipAllowance(OtterCoin otterCoin, String userEmail, String granterAddress, String storeEthAddress, int scholarshipFunds, List<String> receiptList) throws IOException, InterruptedException, ExecutionException {
		User user = userRepository.findByEmail(userEmail);
		Boolean success = blockchainService.spendAllowance(otterCoin, user.getEth_account_id(), granterAddress, storeEthAddress, scholarshipFunds, receiptList);
		return success;
	}
	
	public List<StoreTransaction> getTransactionsHistoryByUserId(String user_email, int number_of_transactions){
		User user = userRepository.findByEmail(user_email);
		List<StoreTransaction> transactionsList = storeTransactionRepository.getMostRecentTransactionsByUserID(user.getId(), number_of_transactions);
		return transactionsList;
	}
}
