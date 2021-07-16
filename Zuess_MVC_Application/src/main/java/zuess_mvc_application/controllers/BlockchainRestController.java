package zuess_mvc_application.controllers;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import zuess_mvc_application.services.*;

/***This is present in case it is needed in the future***/
@RestController
public class BlockchainRestController {

	@Autowired
	BlockchainService blockchainService;
	
	@GetMapping("/api")
	public void testEthereumNetwork(
			@RequestParam("adminPrivateKey") String adminPrivateKey
			) throws InterruptedException, ExecutionException {
	}	
}
