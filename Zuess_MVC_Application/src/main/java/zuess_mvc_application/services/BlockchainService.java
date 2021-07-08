/***BlockchainService contains all the methods used to interact with the 
 * blockchain network, including deploying a Smart Contract, calling Contract
 * functions, and calls to the blockchain not using the Contract.
 * @author Robert Meis
 */
package zuess_mvc_application.services;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Uint;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthAccounts;
import org.web3j.protocol.core.methods.response.EthBlockNumber;
import org.web3j.protocol.core.methods.response.EthGasPrice;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.DefaultGasProvider;

import zuess_mvc_application.domain.*;
import zuess_mvc_application.repository.*;

@Service
public class BlockchainService {
	
	@Autowired
	ScholarshipRepository scholarshipRepository;
	
	Web3j web3j = Web3j.build(new HttpService("HTTP://127.0.0.1:7545"));
	Credentials credentials;
	
	//deployOtterCoin() Deploys OtterCoin instance to blockchain network using account equal to private key holder and returns bool success
	public OtterCoin deploySmartContract(String contractType, String ethPrivateKey, int amount) throws Exception {
		BigInteger initialSupply = BigInteger.valueOf(amount);
		credentials = Credentials.create(ethPrivateKey);
		OtterCoin otterCoin = OtterCoin.deploy(web3j, credentials, new DefaultGasProvider(), initialSupply).send();
		return otterCoin;
	}
	
	/***ERC-20 Methods***/
	public double getContractBalance(OtterCoin otterCoin) throws Exception {
		BigInteger bigIntegerBalance = otterCoin.balanceOf(credentials.getAddress()).send();
		Double balance = bigIntegerBalance.doubleValue();
		System.out.println("\n Balance of Contract: " + balance);
		return balance;
	}
	
	public String getContractName(OtterCoin otterCoin) throws Exception {
		String contractName = otterCoin.name().send();
		System.out.println("\n Smart Contract Name: " + contractName);
		return contractName;
	}
	
	public String getContractSymbol(OtterCoin otterCoin) throws Exception {
		String contractSymbol = otterCoin.symbol().send();
		System.out.println("\n Smart Contract Symbol: " + contractSymbol);
		return contractSymbol;
	}
	
	public String getContractStandard(OtterCoin otterCoin) throws Exception {
		String contractStandard = otterCoin.standard().send();
		System.out.println("\n Smart Contract Standard: " + contractStandard);
		return contractStandard;
	}
	
	//transferFunds() transfers funds. Sender is account of private key holder who deployed OtterCoin
	//calls Smart Contract transfer(address _to, uint256 _value)
	public void transferFunds(OtterCoin otterCoin, List<String> addressList, int amount) throws Exception {
		BigInteger bigIntegerAmount = BigInteger.valueOf(amount);
		for (String address : addressList) {
		TransactionReceipt receipt = otterCoin.transfer(address, bigIntegerAmount).send();
		System.out.print("\n TransactionReceipt: " + receipt + "\n");
		}
	}
	
	//approveAllowance() Approves an allowance (called a "scholarship" in this application) on caller's account to spender account
	//the approved amount can be spent from the caller's funds by the recipient. no funds transfer occurs until the recipient spends the funds
	//see grantScholarship() method in this file
	//calls Smart Contract approve(address _spender, uint256 _value)
	public TransactionReceipt approveAllowance (OtterCoin otterCoin, String spenderAddress, int amount) throws Exception {
		BigInteger bigIntegerAmount = BigInteger.valueOf(amount);
		TransactionReceipt receipt = otterCoin.approve(spenderAddress, bigIntegerAmount).send();
		System.out.print("\n TransactionReceipt: " + receipt + "\n");
		return receipt;
	}
	
	//transferAllowanceFunds() Transfers funds in allowance from allowance holder to to_account
	//calls Smart Contract transferFrom(address _from, address _to, uint256 _value) 
	public TransactionReceipt transferAllowanceFunds (OtterCoin otterCoin, String fromAddress, String toAddress, int amount) throws Exception {
		BigInteger bigIntegerAmount = BigInteger.valueOf(amount);
		TransactionReceipt receipt = otterCoin.transferFrom(fromAddress, toAddress, bigIntegerAmount).send();
		System.out.print("\n TransactionReceipt: " + receipt + "\n");
		return receipt;
	}

	//getBalance() returns balance of account at address
	public BigInteger getBalance(OtterCoin otterCoin, String address) throws Exception {
		BigInteger balance = otterCoin.balanceOf(address).send();
		System.out.println("\n Balance of: " + balance + "\n");
		return balance;
	}
	
	//getBalances() returns List<BigInteger> of balances of accounts list
	public List<BigInteger> getBalances(OtterCoin otterCoin, List<String> addressList) throws Exception {
		List<BigInteger> balanceList = new ArrayList<>();
		
		for (String address : addressList) {
		BigInteger balance = otterCoin.balanceOf(address).send();
		balanceList.add(balance);
		System.out.print("\n Balance of account at address: " + address + " is " + balance + "\n");
		}

		return balanceList;
	}
	
	//getAllowance() returns current allowance
	//calls Smart Contract allowance(mapping(address => mapping(address => uint256))
	public int getAllowance(OtterCoin otterCoin, String granterAddress, String recipientAddress) throws Exception {
		BigInteger allowanceAmount = otterCoin.allowance(granterAddress, recipientAddress).send();
		int allowance = allowanceAmount.intValue();
		
		return allowance;
	}
	
	/***Custom Transactions***/
	//transferFundsUsingCustomFromAddress() calls a Smart Contract method using a specified from address 
	public void transferUsingCustomFromAddress(OtterCoin otterCoin, String fromAddress, String toAddress, int transferAmount) throws IOException, InterruptedException, ExecutionException{
		
		BigInteger gasPrice = otterCoin.GAS_PRICE;
		BigInteger gasLimit = otterCoin.GAS_LIMIT;
		EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(fromAddress, DefaultBlockParameterName.LATEST).send();
		BigInteger nonce = ethGetTransactionCount.getTransactionCount();
				
		Function function = new Function("transfer", // Function name
			 Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(toAddress),
			 new org.web3j.abi.datatypes.Uint(BigInteger.valueOf(transferAmount))), // Function input parameters
			 Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {})
			 );
		String encodedFunction = FunctionEncoder.encode(function);
		
		Transaction transaction = Transaction.createFunctionCallTransaction(fromAddress, nonce, gasPrice, gasLimit, otterCoin.getContractAddress(), 
			 encodedFunction);
		org.web3j.protocol.core.methods.response.EthSendTransaction transactionResponse = web3j.ethSendTransaction(transaction).sendAsync().get();
		String transactionHash = transactionResponse.getTransactionHash();
		
		System.out.println(transactionHash);
	}
	
	/***Direct Calls to Ganache Blockchain. These methods do not call the Smart Contract***/	
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
	 
	public BigInteger getCurrentGasPrice() throws IOException{
		EthGasPrice ethGasPrice = web3j.ethGasPrice().send();
		return ethGasPrice.getGasPrice();
	}
	
}

/***References:
 * https://www.baeldung.com/web3j
 * https://hackernoon.com/ethereum-token-development-using-java-and-web3j-an-overview-spas324r
 * https://github.com/web3j/web3j/issues/833
 * https://www.codota.com/code/java/methods/org.web3j.protocol.Web3j/ethGasPrice
 * 
 ***/
 