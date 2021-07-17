package zuess_mvc_application.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.io.IOException;
import java.math.BigInteger;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpSession;

import zuess_mvc_application.services.*;
import zuess_mvc_application.domain.*;
import zuess_mvc_application.repository.RoleRepository;
import zuess_mvc_application.repository.UserRepository;

@Controller
public class ZuessWebController {
	
	@Autowired
	CustomUserDetailsService customUserDetailsService;
	
	@Autowired 
	StoreTransactionService storeTransactionService;
	
	@Autowired
	ScholarshipService scholarshipService;
	
	@Autowired
	InventoryService inventoryService;
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	RoleRepository roleRepo;
	
	@Autowired
	HttpSession session;
	
	private static BlockchainService blockchainService = new BlockchainService(); //This must be declared as an static or instance variable -- do not @Autowire.
	private static List<String> ethereumAccountsList = blockchainService.getEthereumUserAccounts();
	private static EthereumAccounts ethereumAccounts = new EthereumAccounts(ethereumAccountsList);
	private static String storeEthereumAddress = ethereumAccountsList.get(1);
	private static OtterCoin otterCoin;
	private boolean isContractDeployed = false;
	private String deployedContractType;
	private InputValidator inputValidator = new InputValidator();
	
	List<InventoryItem> cartItemsList = new ArrayList<>();
	
	/***HTTP Routes
	 * Main Site Routes 
	 * ***/
	@GetMapping("")
	public String getHomepage() {
		return "index.html";
	}
	
	@GetMapping("/help")
	public String getHelpPage() {
		return "help_page.html";
	}
	@GetMapping("/registration")
	public String getUserRegistrationForm(User user) throws InterruptedException, ExecutionException {
		return "new_user_registration";
	}
	
	//This allows our custom page to be implemented with Spring Security
	@RequestMapping("/login")
	public String login() {
		return "login";
	}
	
	@GetMapping("/blockchain/view")
	public String getBlockchainVisualization(HttpSession session) throws IOException {
		List<EthereumBlock> ethereumBlocksList = blockchainService.getAllBlocksFromBlockchain();
		session.setAttribute("ethereumBlocksList", ethereumBlocksList);
		return "blockchain";
	}
	
	/***Store Routes
	 * @throws Exception ***/
	@GetMapping("/store")
	public String getStoreHomepage(HttpSession session, Principal principal) throws Exception {	
		
		User user = syncEthereumAndDatabaseAccountBalances(otterCoin, principal.getName());
		List<InventoryItem> inventoryItemsList = inventoryService.getAllInventoryItems();
		List<StoreTransaction> transactionsList = storeTransactionService.getTransactionsHistoryByUserId(principal.getName(), 5);
		int scholarshipBalance = scholarshipService.getScholarshipBalanceFromBlockchain(otterCoin, ethereumAccountsList.get(0), principal.getName());
		session.setAttribute("user", user);
		session.setAttribute("inventoryItemsList", inventoryItemsList);
		session.setAttribute("transactionsList", transactionsList);
		session.setAttribute("scholarshipBalance", scholarshipBalance);

		return "store_homepage";
	}
	
	@PostMapping("/addToCart")
	public String addItemToCart(HttpSession session,
			@RequestParam("itemId") int itemId) {
		
		InventoryItem item = inventoryService.getInventoryItemById(itemId);
		cartItemsList.add(item);
		session.setAttribute("cartItemsList", cartItemsList);
		return "store_homepage";
	}
	
	@GetMapping("/checkout")
	public String getCheckoutPage(HttpSession session) {
		getCartTotal(session);
		session.setAttribute("transactionError", false);
		return "store_checkout";
	}
	
	@PostMapping("/removeCartItem")
	public String removeCartItem(HttpSession session,
			@RequestParam("itemIndex") int itemIndex) {
			cartItemsList.remove(itemIndex);
			session.setAttribute("cartItemsList", cartItemsList);
			getCartTotal(session);
		return "store_checkout";
	}
	
	@PostMapping("/submitNewOrder")
	public String submitNewOrder(HttpSession session, Principal principal,
			@RequestParam(required = false, defaultValue = "0", name = "scholarshipAmount") int scholarshipAmount,
			@RequestParam(required = false, name = "useScholarship") boolean useScholarship
			) throws Exception{
		if (useScholarship != true) scholarshipAmount = 0;
		
		session.setAttribute("transactionError", false);
		session.setAttribute("scholarshipTransactionError", false);
		int total = getCartTotal(session);
		String adminEthereumAddress = ethereumAccountsList.get(0);
		List<String> singleTransactionReceiptsList = new ArrayList<>();
		Boolean scholarshipTransferSuccess = storeTransactionService.spendScholarshipAllowance(otterCoin, principal.getName(), adminEthereumAddress, storeEthereumAddress, scholarshipAmount, singleTransactionReceiptsList);
		
		if (scholarshipTransferSuccess) {
			total = total-scholarshipAmount;
			int scholarshipBalance = scholarshipService.getScholarshipBalanceFromBlockchain(otterCoin, adminEthereumAddress, principal.getName());
			session.setAttribute("scholarshipBalance", scholarshipBalance);
		} else {
			session.setAttribute("scholarshipTransactionError", true);
			return "store_checkout";
		}
		
		Boolean transferSuccess = storeTransactionService.submitNewOrderToEthereum(otterCoin, storeEthereumAddress, principal.getName(), total, singleTransactionReceiptsList);
		if(transferSuccess) {
			StoreTransaction transaction = storeTransactionService.submitNewOrderToDatabase(cartItemsList, scholarshipAmount, principal.getName());
			session.setAttribute("transaction", transaction);
			session.setAttribute("thisTransactionReceipt", singleTransactionReceiptsList);
			cartItemsList.clear();
			return "order_confirmation";
		} else {
			session.setAttribute("transactionError", true);
			return "store_checkout";
		}
	}
	
	/***Admin Portal Routes***/
	@GetMapping("/adminPortal")
	public String getAdminPortal(HttpSession session, Principal principal) throws Exception {
		session.setAttribute("ethereumAccountsList", ethereumAccountsList);
		String email = principal.getName();
		User user = customUserDetailsService.retrieveUserByEmail(email);
		session.setAttribute("deployed", isContractDeployed);
		if (isContractDeployed && otterCoin != null) {
			double contractBalance = blockchainService.getContractBalance(otterCoin);
			session.setAttribute("contractType", deployedContractType);
			session.setAttribute("contractBalance", contractBalance);
		}
		session.setAttribute("user", user);
		session.setAttribute("modalToShow", null);
		return "admin_portal";
	}
	
	@PostMapping("/deploySmartContract")
	public String deploySmartContract(HttpSession session,
			@RequestParam("contractType") String contractType,
			@RequestParam(required = false, defaultValue = "0", name = "initialContractFunds") int initialContractFunds,
			@RequestParam("ethPrivateKey") String ethPrivateKey
			) throws Exception {
		
		if ( (!inputValidator.validateSingleField(ethPrivateKey, "ethPrivateKey")) 
				|| !inputValidator.notEmpty(contractType, initialContractFunds, ethPrivateKey)) return "admin_portal";

		otterCoin = blockchainService.deploySmartContract(contractType, ethPrivateKey, initialContractFunds);
		if (otterCoin != null) isContractDeployed = true;
		deployedContractType = contractType;
		double contractBalance = blockchainService.getContractBalance(otterCoin);
		
		session.setAttribute("ethereumAccountsList", ethereumAccountsList);
		session.setAttribute("deployed", isContractDeployed);
		session.setAttribute("contractType", deployedContractType);
		session.setAttribute("contractBalance", contractBalance);
		
		User user = (User) session.getAttribute("user");
		user = customUserDetailsService.syncEthereumAndDatabaseUserBalances(otterCoin, user);	
		return "admin_portal";
	}
	
	@PostMapping("/transferFunds")
	public String transferFundsToEthAccounts(HttpSession session,
			@RequestParam("accounts") List<String> accounts,
			@RequestParam(required = false, defaultValue = "0", name = "transferAmount") int transferAmount
			) throws Exception {
		
		if (!inputValidator.validateSingleField(transferAmount, "amount")) return "admin_portal";
		
		List<TransactionReceipt> transactionReceiptList = blockchainService.transferFunds(otterCoin, accounts, transferAmount);
		for (TransactionReceipt receipt : transactionReceiptList) {
			System.out.println("Tx " + receipt.getTransactionHash());
		}
		
		double contractBalance = blockchainService.getContractBalance(otterCoin);
		session.setAttribute("transferAmount", transferAmount);
		session.setAttribute("transactionReceiptList", transactionReceiptList);
		session.setAttribute("modalToShow", "fundsTransferModal");
		session.setAttribute("contractBalance", contractBalance);

		return "admin_portal";
	}
	
	//If feasible, this should eventually replace all above PostMapping routes by using Optional Request Params
	//Or we could look at using Ajax
	@PostMapping("/adminActions")
	public String administratorActions(HttpSession session,
			@RequestParam("getBalanceOfAddress") Optional<String> balanceAddress
			) throws Exception {
		
		if (!inputValidator.validateSingleField(balanceAddress, "ethAddress")) return "admin_portal";
		if (balanceAddress != null) {
		BigInteger accountBalance = blockchainService.getBalance(otterCoin, balanceAddress.get());
		session.setAttribute("accountBalance", accountBalance);
		session.setAttribute("modalToShow", null);
		}
		return "admin_portal";
	}

	@PostMapping("/grantScholarship")
	public String grantScholarship(HttpSession session,
			@RequestParam("recipientEmail") String email,
			@RequestParam(required = false, defaultValue = "0", name = "scholarshipAmount") int amount) throws Exception {
		
		if (!inputValidator.validateSingleField(email, "email")) return "admin_portal";
		
		User user = customUserDetailsService.retrieveUserByEmail(email);
		if (user == null) {
			session.setAttribute("userFound", false);
		} else {
			
			int recipient_id = user.getId();
			String recipient_eth_id = user.getEth_account_id();
			int scholarshipAmount = amount;
			TransactionReceipt scholarshipReceipt = scholarshipService.grantNewScholarship(otterCoin, recipient_id, recipient_eth_id, scholarshipAmount, null);
			session.setAttribute("scholarshipReceipt", scholarshipReceipt);
			session.setAttribute("scholarshipAmount", amount);
		}
		
		scholarshipService.syncEthereumAndDatabaseAllowances(otterCoin, ethereumAccountsList.get(0));
		List<Scholarship> scholarshipsList = scholarshipService.getActiveScholarships();
		session.setAttribute("modalToShow", "showScholarshipReceiptModal");
		session.setAttribute("scholarshipsList", scholarshipsList);
		return "admin_portal";
	}
	
	@GetMapping("/viewScholarships")
	public String viewScholarships(HttpSession session) throws Exception {
		List<Scholarship> scholarshipsList = scholarshipService.getActiveScholarships();
		session.setAttribute("scholarshipsList", scholarshipsList);
		session.setAttribute("modalToShow", null);
		return "admin_portal";
	}
	
	/***User Account Routes***/
// TODO: Prevent repeated information sign ups (ensure email is unique, return error if already present)
	@PostMapping("/registration/submit")
	public String persistNewUser(HttpSession session, User user) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setEth_account_id(ethereumAccounts.assignNewEthereumAccount());
		
		//TODO: This is where i will insert logic to select what type of role will be created. I think creating a default admin role that auto populates and limiting demo to user role creation and leaving this be makes the most sense.
		String userRole = "USER";
		user.setRoles(Arrays.asList(roleRepo.findByName(userRole)));
		userRepo.save(user);
		session.setAttribute("user", user);
		return "acct_create_success";
	}
	 
	@GetMapping("/accountInfo")
	public String getUserAccountDetails(Principal principal) throws Exception {
		
		User user = syncEthereumAndDatabaseAccountBalances(otterCoin, principal.getName());
		List<Scholarship> currentUserScholarshipsList = scholarshipService.getScholarshipsByUserId(user.getId());
		int scholarshipBalance = scholarshipService.getScholarshipBalanceFromBlockchain(otterCoin, ethereumAccountsList.get(0), principal.getName());
		session.setAttribute("scholarshipsList", currentUserScholarshipsList);
		session.setAttribute("user", user);
		session.setAttribute("scholarshipBalance", scholarshipBalance);
		session.setAttribute("modalToShow", null);
		return "standard_user_account";
	}
	
	@PostMapping("/userFundsTransfer")
	public String userActions(Principal principal,
			@RequestParam("ethToAddress") String ethToAddress,
			@RequestParam(required = false, defaultValue = "0", name = "transferAmount") int transferAmount
			) throws Exception {
		
			if (!inputValidator.validateSingleField(ethToAddress, "ethAddress")) return "standard_user_account";
				
			String email = principal.getName();
			User user = customUserDetailsService.retrieveUserByEmail(email);
			List<String> transferReceipts = new ArrayList<>();
			String fromAddress = user.getEth_account_id();
			
			blockchainService.transferUsingCustomFromAddress(otterCoin, fromAddress, ethToAddress, transferAmount, transferReceipts);
			session.setAttribute("transferAmount", transferAmount);
			session.setAttribute("userFundsTransferRecipient", user);
			session.setAttribute("transferReceipts", transferReceipts);
			session.setAttribute("modalToShow", "transferReceiptsModal");
		
			return "standard_user_account";
	}
	

	/***Custom Controller Functions***/
	
	//InputValidator
	//This is an experimental class to validate form input fields of multiple web forms and variable types in the same function.
	//Server-side input validation is done in our application for security purposes -- any client-side input validation can be bypassed.
	//@Author Robert Meis
	public class InputValidator {
		
		public InputValidator() { }
		
		public <T> boolean notEmpty(T ... parameters) {
			for (T parameter : parameters) {
				
					String pattern = "(^ +$|^$)";
					Pattern regex = Pattern.compile(pattern);
					Matcher matcher = regex.matcher(parameter.toString());
					if (matcher.find()) return false;	
				}
			
			return true;
			}
		
		public <T> boolean validateSingleField(T fieldText, String fieldType ) {
			boolean isValidInput = true;
			
				switch(fieldType) {
					case "ethPrivateKey":
						String pattern = "[A-Fa-f0-9]{64}";
						Pattern regex = Pattern.compile(pattern);
						Matcher matcher = regex.matcher(fieldText.toString());
						if (!matcher.find()) isValidInput = false;
						break;
					
					case "ethAddress":
						pattern = "0x[A-Fa-f0-9]{40}";
						regex = Pattern.compile(pattern);
						matcher = regex.matcher(fieldText.toString());
						if (!matcher.find()) isValidInput = false;
						break;
						
					case "email":
						pattern = "[A-Za-z0-9\\.-]+@[A-Za-z0-9]+\\.[A-Za-z]{3}";
						regex = Pattern.compile(pattern);
						matcher = regex.matcher(fieldText.toString());
						if (!matcher.find()) isValidInput = false;
						break;
					
					case "amount":
						pattern = "^[0-9]+$";
						regex = Pattern.compile(pattern);
						matcher = regex.matcher(fieldText.toString());
						if (!matcher.find()) isValidInput = false;
						break;
						
					default: //checks form field list for blank input or whitespace only
						pattern = "(^ +$|^$)";
						regex = Pattern.compile(pattern);
						matcher = regex.matcher(fieldText.toString());
						if (matcher.find()) isValidInput = false;
			}
			
			return isValidInput;
			}
		
	public boolean validateFieldList(List<?> fieldTextList, String ... fieldTypes) {
		boolean isValidInput = true;
		
		int iteration = 0;
		for (String type : fieldTypes) {
			
			switch(type) {
				case "ethPrivateKey":
					String pattern = "[A-Fa-f0-9]{64}";
					Pattern regex = Pattern.compile(pattern);
					Matcher matcher = regex.matcher(fieldTextList.get(iteration).toString());
					if (!matcher.find()) isValidInput = false;
					break;
				
				case "ethAddress":
					pattern = "0x[A-Fa-f0-9]{40}";
					regex = Pattern.compile(pattern);
					matcher = regex.matcher(fieldTextList.get(iteration).toString());
					if (!matcher.find()) isValidInput = false;
					break;
					
				case "email":
					pattern = "[A-Za-z0-9\\.-]+@[A-Za-z0-9]+\\.[A-Za-z]{3}";
					regex = Pattern.compile(pattern);
					matcher = regex.matcher(fieldTextList.get(iteration).toString());
					if (!matcher.find()) isValidInput = false;
					break;
				
				case "amount":
					pattern = "^[0-9]+$";
					regex = Pattern.compile(pattern);
					matcher = regex.matcher(fieldTextList.get(iteration).toString());
					if (!matcher.find()) isValidInput = false;
					break;
			
				default: //checks form field list for blank input or whitespace only
					pattern = "(^ +$|^$)";
					regex = Pattern.compile(pattern);
					matcher = regex.matcher(fieldTextList.get(iteration).toString());
					if (matcher.find()) isValidInput = false;	
					break;
			}
			iteration++;
		}
		
		return isValidInput;
		}
	
	}
	
	
	
	public int getCartTotal(HttpSession session) {
		int total = 0;
		int scholarshipEligibleAmount = 0; 
		for (InventoryItem item : cartItemsList) {
			total += item.getPrice();
			
			if (item.isScholarship_eligible()) {
				scholarshipEligibleAmount += item.getPrice();
			}
		}
		session.setAttribute("total", total);
		session.setAttribute("scholarshipEligibleAmount", scholarshipEligibleAmount);
		return total;
	}
	
	public User syncEthereumAndDatabaseAccountBalances(OtterCoin otterCoin, String userEmail) throws Exception {
			User user = customUserDetailsService.retrieveUserByEmail(userEmail);
			user = customUserDetailsService.syncEthereumAndDatabaseUserBalances(otterCoin, user);
			scholarshipService.syncEthereumAndDatabaseAllowances(otterCoin, ethereumAccountsList.get(0));
			return user;
	}
	
}
