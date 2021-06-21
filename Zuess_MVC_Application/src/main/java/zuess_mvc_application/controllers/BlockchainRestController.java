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
		
		final String ethAddress = "0x13d8b9F792d10D31b90413B3BdC7d1f90726a71F";
		blockchainService.getBlockNumber();
		blockchainService.getEthAccounts();
		//blockchainService.ethGetBalance(ethAddress);
	
	}

}
