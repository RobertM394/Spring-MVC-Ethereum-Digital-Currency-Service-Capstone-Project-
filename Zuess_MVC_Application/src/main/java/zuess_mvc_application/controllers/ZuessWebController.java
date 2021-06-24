package zuess_mvc_application.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.servlet.http.HttpSession;

import zuess_mvc_application.services.*;
import zuess_mvc_application.domain.*;
import zuess_mvc_application.repository.UserRepository;

@Controller
public class ZuessWebController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	BlockchainService blockchainService;
	
	@Autowired
	HttpSession session;
	
	@Autowired
	UserRepository userRepo;
	
	/***HTTP Routes
	 * @throws ExecutionException 
	 * @throws InterruptedException ***/
	@GetMapping("/registration")
	public String getUserRegistrationForm(Model model) throws InterruptedException, ExecutionException {
		model.addAttribute("user", new User());
		return "new_user_registration";
	}
	
// TODO: Prevent repeated information signups (ensure email is unique, return error if already present)
	@PostMapping("/submitNewUserRegistration")
	public String persistNewUser(Model model, User user) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		userRepo.save(user);
		return "acct_create_success";
	}
	
	@GetMapping("/accountInfo")
	public String getUserAccountDetails() {
		return "standard_user_account";
	}
	
	@GetMapping("")
	public String getHomepage() {
		return "index.html";
	}
	
	@GetMapping("/adminPortal")
	public String getAdminPortal() {
		return "admin_portal";
	}
	
	@PostMapping("/deploySmartContract")
	public String deploySmartContract(Model model,
			@RequestParam("contractType") String contractType,
			@RequestParam("initialContractFunds") int initialContractFunds,
			@RequestParam("ethPrivateKey") String ethPrivateKey
			) {
		
		try {
		blockchainService.deploySmartContract(contractType, ethPrivateKey, initialContractFunds);
		List<String> ethereumAccountsList = blockchainService.getEthereumUserAccounts();
		model.addAttribute("deployed", true);
		model.addAttribute("contractType", contractType);
		model.addAttribute("ethereumAccountsList", ethereumAccountsList);
		} catch (Exception ex) {
			model.addAttribute("deployed", false);
			ex.printStackTrace();
		}
		return "admin_portal";
	}
	
}
