package zuess_mvc_application.controllers;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import zuess_mvc_application.services.*;

@RestController
public class BlockchainRestController {

	@Autowired
	BlockchainService blockchainService;
	
	@GetMapping("/api/test")
	public void testEthereumNetwork(
			@RequestParam("adminPrivateKey") String adminPrivateKey
			) throws InterruptedException, ExecutionException {
		
		/***Deploy Smart Contract and test call methods on deployed Smart Contract***/
		/***
		try
		{
			
			String ADMIN_ACCOUNT = "0x68c176f609F41A62D3473c6bdD279f47d0508E6e";
			String TEST_ACCOUNT = "0x31866788BaE0df59B11f93e48aA9e896b3A53A5D";
			int STARTING_BALANCE = 1000;
			
			//Contract info functions
			blockchainService.deploySmartContract(null, adminPrivateKey, STARTING_BALANCE);
			blockchainService.getContractName();
			blockchainService.getContractSymbol();
			blockchainService.getContractStandard();
			blockchainService.getContractBalance();
			
			//Fund transfer functions
			blockchainService.transferFunds(TEST_ACCOUNT, 10);
			blockchainService.getBalance(TEST_ACCOUNT);
			blockchainService.approveAllowance(ADMIN_ACCOUNT, 100);
			blockchainService.transferAllowanceFunds(ADMIN_ACCOUNT, TEST_ACCOUNT, 50);
			blockchainService.getBalance(TEST_ACCOUNT);
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		***/
	}	
}
