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
		try
		{
			String ADMIN_ACCOUNT = "0x738F657A0e9F102528c1160E04554a89747aAa1e";
			String TEST_ACCOUNT = "0xc37F977738df1bC264D26FF2e906471Bf222Ea63";
			
			//Contract info functions
			blockchainService.deploySmartContract(adminPrivateKey);
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
		
		/*** blockchainService.getBlockNumber();
		blockchainService.getEthAccounts();
		blockchainService.ethGetBalance(ethAddress);
		***/
		
	
	}

}
