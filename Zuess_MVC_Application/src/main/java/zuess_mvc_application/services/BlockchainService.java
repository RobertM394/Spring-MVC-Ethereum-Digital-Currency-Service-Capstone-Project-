package zuess_mvc_application.services;

import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthAccounts;
import org.web3j.protocol.core.methods.response.EthBlockNumber;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.http.HttpService;

@Service
public class BlockchainService {
	
	
	Web3j web3j = Web3j.build(new HttpService("HTTP://127.0.0.1:7545"));
	
	public EthBlockNumber getBlockNumber() throws InterruptedException, ExecutionException {
	    EthBlockNumber result = new EthBlockNumber();
	    result = this.web3j.ethBlockNumber()
	      .sendAsync()
	      .get();
	    
	    System.out.print("\n Blocknumber: " + result + "\n");
	    return result;
	}	
	
	//Get balance of specified account
	//Returns error currently, needs debugging
	public EthGetBalance ethGetBalance(String address) {
		EthGetBalance result = new EthGetBalance();
		
		try {
		this.web3j.ethGetBalance(address, DefaultBlockParameter.valueOf("latest"))
		.sendAsync()
		.get();
		}	catch (Exception ex) {
			System.out.print("\n ***Exception: " + ex + "\n");
			}
		
		BigInteger wei = result.getBalance();
		System.out.print("\n Balance: " + wei + "\n");
		
		return result;
	}
	
	 public List<String> getEthAccounts() {
       EthAccounts result = new EthAccounts();
       try {
           result = this.web3j.ethAccounts()
                   .sendAsync()
                   .get();
       } catch (Exception e) {
           e.printStackTrace();
       }
       
       System.out.print("\n Accounts: " + result.getAccounts() + "\n");
       
       return result.getAccounts();
	
	 }
}

//

/***References:
 * https://www.baeldung.com/web3j
 * 
 ***/
 