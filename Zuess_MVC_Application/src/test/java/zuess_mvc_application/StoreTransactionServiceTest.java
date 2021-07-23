package zuess_mvc_application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import zuess_mvc_application.domain.*;
import zuess_mvc_application.repository.*; 
import zuess_mvc_application.services.*;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

@SpringBootTest
public class StoreTransactionServiceTest {

	@MockBean
	StoreTransactionRepository storeTransactionRepository;
	
	@MockBean
	UserRepository userRepository;
	
	@MockBean
	BlockchainService blockchainService;
	
	@Autowired
	StoreTransactionService storeTransactionService;
	
	@BeforeEach
	public void setUpEach() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void submitNewOrderToDatabaseShouldReturnTransaction() {
		
		String itemName = "Mastering Regular Expressions";
		List<InventoryItem> cartItemsList = new ArrayList<InventoryItem>();
		InventoryItem item = new InventoryItem(true, itemName, "Textbook", "Author: Friedl", 100, "img");
		cartItemsList.add(item);
		
		User user = new User("email@email.com", "password", "Robert", "Meis", "0xD662C01D53852754AE880002d5f58E2f5D339A74", 25, 0);
		user.setId(1);
		given(userRepository.findByEmail("email@email.com")).willReturn(user);
	
		StoreTransaction actualResult = storeTransactionService.submitNewOrderToDatabase(cartItemsList, 100, "email@email.com");
		assertThat(actualResult).isNotNull();
	}
	
	@Test
	public void submitNewOrderToEthereumShouldReturnBoolSuccess() throws IOException, InterruptedException, ExecutionException {
		List<String> receiptList = new ArrayList<>();
		String receiptHash = "0xe1ede6913f81eaa62a08ec57f63fee92efcc68aa0e329128976ed6fec5bc1d86";
		receiptList.add(receiptHash);
		User user = new User("email@email.com", "password", "Robert", "Meis", "0xD662C01D53852754AE880002d5f58E2f5D339A74", 25, 0);
		String storeEthereumAddress = "0xD662C01D53852754AE880002d5f58E2f5D339A74";
		
		given(userRepository.findByEmail("email@email.com")).willReturn(user);
		given(blockchainService.transferUsingCustomFromAddress(null, user.getEth_account_id(), storeEthereumAddress, 100, receiptList)).willReturn(true);
		
		boolean actualResult = storeTransactionService.submitNewOrderToEthereum(null, storeEthereumAddress, user.getEmail(), 100, receiptList);
		assertEquals(actualResult, true);
	}
	
	@Test
	public void spendScholarshipAllowanceShouldReturnBoolSuccess() throws IOException, InterruptedException, ExecutionException {
		OtterCoin otterCoin = null;
		String granterAddress = "0xD662C01D53852754AE880002d5f58E2f5D339A74";
		String storeEthAddress = "0xBE3c7e5438b19d68868003F8390BDA18F63E8D72";
		int scholarshipFunds = 100;
		
		List<String> receiptList = new ArrayList<>();
		String receiptHash = "0xe1ede6913f81eaa62a08ec57f63fee92efcc68aa0e329128976ed6fec5bc1d86";
		receiptList.add(receiptHash);
		
		User user = new User("email@email.com", "password", "Robert", "Meis", "0xD662C01D53852754AE880002d5f58E2f5D339A74", 25, 0);
		
		given(userRepository.findByEmail("email@email.com")).willReturn(user);
		given(blockchainService.spendAllowance(otterCoin, user.getEth_account_id(), granterAddress, storeEthAddress, scholarshipFunds, receiptList)).willReturn(true);
		Boolean result = storeTransactionService.spendScholarshipAllowance(otterCoin, "email@email.com", granterAddress, storeEthAddress, scholarshipFunds, receiptList);
		assertEquals(result, true);
	}
	
	@Test
	public void getTransactionHistoryByUserIdShouldReturnTransactionsList() {
		List<StoreTransaction> transactionsList = new ArrayList<StoreTransaction>();
		java.sql.Date now = new Date(System.currentTimeMillis());
		StoreTransaction transaction = new StoreTransaction(1, 100, 100, "Item", now);
		transactionsList.add(transaction);
		
		User user = new User("email@email.com", "password", "Robert", "Meis", "0xD662C01D53852754AE880002d5f58E2f5D339A74", 25, 0);
		user.setId(0);
		
		given(userRepository.findByEmail("email@email.com")).willReturn(user);
		given(storeTransactionRepository.getMostRecentTransactionsByUserID(0, 5)).willReturn(transactionsList);
		
		List<StoreTransaction> actualListResult = storeTransactionService.getTransactionsHistoryByUserId(user.getEmail(), 5);
		assertEquals(actualListResult, transactionsList);
	}
	
}
