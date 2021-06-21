package zuess_mvc_application.controllers;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import zuess_mvc_application.services.*;

@RestController
public class BlockchainRestController {

	@Autowired
	BlockchainService blockchainService;
	
	@GetMapping("/api/test")
	public void callEthereumNetwork() throws InterruptedException, ExecutionException {
		
		/***Deploy Smart Contract and test call methods on deployed Smart Contract***/
		try
		{
			blockchainService.deployOtterCoin("");
			blockchainService.getContractBalance();
			blockchainService.transferFunds("0xdaEc996523902b545BF564155FCdC1F235C7de6A", 10);
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
