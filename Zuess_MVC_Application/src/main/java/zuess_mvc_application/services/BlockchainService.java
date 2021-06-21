/***BlockchainService contains all the methods used to interact with the 
 * blockchain network, including deploying a Smart Contract, calling Contract
 * functions, and calls to the blockchain not using the Contract.
 * @author Robert Meis
 */
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
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.DefaultGasProvider;

import zuess_mvc_application.domain.OtterCoin;

@Service
public class BlockchainService {
	
	/***Global Variables***/
	Web3j web3j = Web3j.build(new HttpService("HTTP://127.0.0.1:7545"));
	OtterCoin otterCoin;
	Credentials credentials;
	
	/***Deploy OtterCoin Contract to Network***/
	//deployOtterCoin()
	//
	//Deploys OtterCoin instance to blockchain network using account equal to private key holder
	public void deploySmartContract(String ethPrivateKey) throws Exception {
	BigInteger initialSupply = BigInteger.valueOf(1000);
	String privateKey = "e618eb6d3f99750892fe50dd554eaa754d356ed8fe7da12e9b1ba0ce04c5b17b"; //private key of account 0 on Ganache blockchain
	credentials = Credentials.create(privateKey);
	otterCoin = OtterCoin.deploy(web3j, credentials, new DefaultGasProvider(), initialSupply).send();
	}
	
	/***Method calls to Deployed Smart Contract***/
	//getContractBalance()
	//
	//Returns balance of deployed Smart Contract
	public BigInteger getContractBalance() throws Exception {
	BigInteger balance = otterCoin.balanceOf(credentials.getAddress()).send();
	System.out.println("\n Balance of Contract: " + balance);
	return balance;
	}
	
	//transferFunds()
	//
	//Calls OtterCoin transfer() function
	//Transfers funds from one account to another and returns TransactionReceipt
	public TransactionReceipt transferFunds(String to_address, int amount) throws Exception {
		BigInteger bigIntegerAmount = BigInteger.valueOf(amount);
		TransactionReceipt receipt = otterCoin.transfer(to_address, bigIntegerAmount).send();
		System.out.print("\n TransactionReceipt: " + receipt + "\n");
		return receipt;
	}

	/***Direct Calls to Ganache Blockchain
	 * These methods do not call the Smart Contract***/
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
 * https://hackernoon.com/ethereum-token-development-using-java-and-web3j-an-overview-spas324r
 * 
 ***/
 