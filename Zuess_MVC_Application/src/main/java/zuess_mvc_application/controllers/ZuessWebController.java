package zuess_mvc_application.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigInteger;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.concurrent.ExecutionException;

import javax.servlet.http.HttpSession;

import zuess_mvc_application.services.*;
import zuess_mvc_application.domain.*;
import zuess_mvc_application.repository.UserRepository;

@Controller
public class ZuessWebController {
	
	@Autowired
	CustomUserDetailsService customUserDetailsService;
	
	@Autowired
	ScholarshipService scholarshipService;
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	HttpSession session;
	
	private static BlockchainService blockchainService = new BlockchainService(); //This must be declared as an static or instance variable -- do not @Autowire.
	private static List<String> ethereumAccountsList = blockchainService.getEthereumUserAccounts();
	private static EthereumAccounts ethereumAccounts = new EthereumAccounts(ethereumAccountsList);
	private static OtterCoin otterCoin;
	
	/***HTTP Routes***/
	@GetMapping("/registration")
	public String getUserRegistrationForm(User user) throws InterruptedException, ExecutionException {
		return "new_user_registration";
	}
	
// TODO: Prevent repeated information sign ups (ensure email is unique, return error if already present)
	@PostMapping("/submitNewUserRegistration")
	public String persistNewUser(HttpSession session, User user) {
		
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setEth_account_id(ethereumAccounts.assignNewEthereumAccount());
		System.out.println("Ethereum Account assigned: " + user.getEth_account_id());
		userRepo.save(user);
		session.setAttribute("user", user);
		return "acct_create_success";
	}
	 
	@GetMapping("/accountInfo")
	public String getUserAccountDetails(Principal principal) throws Exception {
		String email = principal.getName();
		User user = customUserDetailsService.retrieveUserByEmail(email);
		user = customUserDetailsService.syncEthereumAndDatabaseUserBalances(otterCoin, user);
		session.setAttribute("user", user);
		return "standard_user_account";
	}
	
	@GetMapping("")
	public String getHomepage() {
		return "index.html";
	}
	//This allows our custom page to be implemented with spring security
	@RequestMapping("/login")
	public String login() {
		return "login";
	}
	
	
	@GetMapping("/adminPortal")
	public String getAdminPortal(HttpSession session, Principal principal) throws Exception {
		session.setAttribute("ethereumAccountsList", ethereumAccountsList);
		String email = principal.getName();
		User user = customUserDetailsService.retrieveUserByEmail(email);
		session.setAttribute("user", user);
		return "admin_portal";
	}
	
	@PostMapping("/deploySmartContract")
	public String deploySmartContract(HttpSession session,
			@RequestParam("contractType") String contractType,
			@RequestParam("initialContractFunds") int initialContractFunds,
			@RequestParam("ethPrivateKey") String ethPrivateKey
			) throws Exception {
		
		otterCoin = blockchainService.deploySmartContract(contractType, ethPrivateKey, initialContractFunds);
		double contractBalance = blockchainService.getContractBalance(otterCoin);
		session.setAttribute("ethereumAccountsList", ethereumAccountsList);
		session.setAttribute("deployed", true);
		session.setAttribute("contractType", contractType);
		session.setAttribute("contractBalance", contractBalance);
		User user = (User) session.getAttribute("user");
		user = customUserDetailsService.syncEthereumAndDatabaseUserBalances(otterCoin, user);
		return "admin_portal";
	}
	
	@PostMapping("/transferFunds")
	public String transferFundsToEthAccounts(HttpSession session,
			@RequestParam("accounts") List<String> accounts,
			@RequestParam("transferAmount") int transferAmount
			) throws Exception {
		blockchainService.transferFunds(otterCoin, accounts, transferAmount);
		double contractBalance = blockchainService.getContractBalance(otterCoin);
		session.setAttribute("contractBalance", contractBalance);

		return "admin_portal";
	}
	
	//If feasible, this should eventually replace all above PostMapping routes by using Optional Request Params
	//Or we could look at using Ajax
	@PostMapping("/adminActions")
	public String administratorActions(HttpSession session,
			@RequestParam("getBalanceOfAddress") Optional<String> balanceAddress
			) throws Exception {
		
		if (balanceAddress != null) {
		BigInteger accountBalance = blockchainService.getBalance(otterCoin, balanceAddress.get());
		session.setAttribute("accountBalance", accountBalance);
		}
		return "admin_portal";
	}

	@PostMapping("/grantScholarship")
	public String grantScholarship(HttpSession session,
			@RequestParam("recipientEmail") String email,
			@RequestParam("scholarshipAmount") int amount) throws Exception {
		
		User user = customUserDetailsService.retrieveUserByEmail(email);
		if (user == null) {
			session.setAttribute("userFound", false);
		} else {
			
			int recipient_id = user.getId();
			String recipient_eth_id = user.getEth_account_id();
			int scholarshipAmount = amount;
			Scholarship scholarship = scholarshipService.grantNewScholarship(otterCoin, recipient_id, recipient_eth_id, scholarshipAmount, null);
		}
		
		return "admin_portal";
	}
	
	@PostMapping("/viewScholarships")
	public String viewScholarships(HttpSession session) throws Exception {
		List<Scholarship> scholarshipsList = scholarshipService.getActiveScholarships();
		session.setAttribute("scholarshipsList", scholarshipsList);
		session.setAttribute("userFound", true);
		return "admin_portal";
	}
	
	
	/***User Account Routes***/
	@PostMapping("/userFundsTransfer")
	public String userActions(Principal principal,
			@RequestParam("ethToAddress") String ethToAddress,
			@RequestParam("transferAmount") int transferAmount
			) throws Exception {
		
			String FROM_ADDRESS = "0x6653089355F411b2eEb7ba45912317a4b2F57a40";
			String TO_ADDRESS = "0x0343A271bA0D711975C5AEb1EB97EEB9eCc8d0b7";
			
			String email = principal.getName();
			User user = customUserDetailsService.retrieveUserByEmail(email);
			blockchainService.transferFundsAsStandardUser(FROM_ADDRESS, TO_ADDRESS, 10);
			
		return "standard_user_account";
	}
	
	
}
