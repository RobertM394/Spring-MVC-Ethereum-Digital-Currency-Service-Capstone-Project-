/***BlockchainService contains all the methods used to interact with the 
 * blockchain network, including deploying a Smart Contract, calling Contract
 * functions, and calls to the blockchain not using the Contract.
 * @author Robert Meis
 */
package zuess_mvc_application.services;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;


import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;

import org.web3j.protocol.core.methods.response.EthAccounts;

import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.DefaultGasProvider;

import zuess_mvc_application.domain.*;

@Service
public class BlockchainService {
	
	Web3j web3j = Web3j.build(new HttpService("HTTP://127.0.0.1:7545"));
	Credentials credentials;
	
	//deployOtterCoin()
	//
	//Deploys OtterCoin instance to blockchain network using account equal to private key holder and returns bool success
	public OtterCoin deploySmartContract(String contractType, String ethPrivateKey, int amount) throws Exception {
		BigInteger initialSupply = BigInteger.valueOf(amount);
		credentials = Credentials.create(ethPrivateKey);
		OtterCoin otterCoin = OtterCoin.deploy(web3j, credentials, new DefaultGasProvider(), initialSupply).send();
		return otterCoin;
	}
	
	/***ERC-20 Methods***/
	//getContractBalance()
	//
	//Returns balance of deployed Smart Contract as double
	public double getContractBalance(OtterCoin otterCoin) throws Exception {
		BigInteger bigIntegerBalance = otterCoin.balanceOf(credentials.getAddress()).send();
		Double balance = bigIntegerBalance.doubleValue();
		System.out.println("\n Balance of Contract: " + balance);
		return balance;
	}
	
	//getContractName()
	//
	//Returns the name of the Smart Contract as a String
	public String getContractName(OtterCoin otterCoin) throws Exception {
		String contractName = otterCoin.name().send();
		System.out.println("\n Smart Contract Name: " + contractName);
		return contractName;
	}
	
	//getContractName()
	//
	//Returns the symbol of the Smart Contract as a String
	public String getContractSymbol(OtterCoin otterCoin) throws Exception {
		String contractSymbol = otterCoin.symbol().send();
		System.out.println("\n Smart Contract Symbol: " + contractSymbol);
		return contractSymbol;
	}
	
	//getContractName()
	//
	//Returns the standard/version of the Smart Contract as a String
	public String getContractStandard(OtterCoin otterCoin) throws Exception {
		String contractStandard = otterCoin.standard().send();
		System.out.println("\n Smart Contract Standard: " + contractStandard);
		return contractStandard;
	}
	
	//transferFunds()
	//
	//Calls Smart Contract transfer() function
	//Transfers funds from one account to another and returns TransactionReceipt
	public void transferFunds(OtterCoin otterCoin, List<String> addressList, int amount) throws Exception {
		BigInteger bigIntegerAmount = BigInteger.valueOf(amount);
		for (String address : addressList) {
		TransactionReceipt receipt = otterCoin.transfer(address, bigIntegerAmount).send();
		System.out.print("\n TransactionReceipt: " + receipt + "\n");
		}
	}
	
	//approveAllowance()
	//
	//Calls Smart Contract approve() function
	//Approves an allowance on caller's account to spender account
	public TransactionReceipt approveAllowance (OtterCoin otterCoin, String spenderAddress, int amount) throws Exception {
		BigInteger bigIntegerAmount = BigInteger.valueOf(amount);
		TransactionReceipt receipt = otterCoin.approve(spenderAddress, bigIntegerAmount).send();
		System.out.print("\n TransactionReceipt: " + receipt + "\n");
		return receipt;
	}
	
	//transferAllowanceFunds()
	//
	//Calls Smart Contract transferFrom() function
	//Transfers funds in allowance from allowance holder to to_account
	public TransactionReceipt transferAllowanceFunds (OtterCoin otterCoin, String fromAddress, String toAddress, int amount) throws Exception {
		BigInteger bigIntegerAmount = BigInteger.valueOf(amount);
		TransactionReceipt receipt = otterCoin.transferFrom(fromAddress, toAddress, bigIntegerAmount).send();
		System.out.print("\n TransactionReceipt: " + receipt + "\n");
		return receipt;
	}

	//getBalance()
	//
	//Calls Smart Contract balanceOf() function using account address
	//Returns single account balance as BigInteger
	public BigInteger getBalance(OtterCoin otterCoin, String address) throws Exception {
		BigInteger balance = otterCoin.balanceOf(address).send();
		System.out.println("\n Balance of: " + balance + "\n");
		return balance;
	}
	
	//getBalances()
	//
	//Calls Smart Contract balanceOf() function
	//Returns List<BigInteger> of balances of accounts list
	public List<BigInteger> getBalances(OtterCoin otterCoin, List<String> addressList) throws Exception {
		List<BigInteger> balanceList = new ArrayList<>();
		
		for (String address : addressList) {
		BigInteger balance = otterCoin.balanceOf(address).send();
		balanceList.add(balance);
		System.out.print("\n Balance of account at address: " + address + " is " + balance + "\n");
		}

		return balanceList;
	}
	
	/***Direct Calls to Ganache Blockchain
	 * These methods do not call the Smart Contract***/	
	 public List<String> getEthereumUserAccounts() {
       EthAccounts accounts = new EthAccounts();
       try {
           accounts = this.web3j.ethAccounts()
                   .sendAsync()
                   .get();
       } catch (Exception e) {
           e.printStackTrace();
       }
       return accounts.getAccounts();
	 }
}

//

/***References:
 * https://www.baeldung.com/web3j
 * https://hackernoon.com/ethereum-token-development-using-java-and-web3j-an-overview-spas324r
 * 
 ***/
 