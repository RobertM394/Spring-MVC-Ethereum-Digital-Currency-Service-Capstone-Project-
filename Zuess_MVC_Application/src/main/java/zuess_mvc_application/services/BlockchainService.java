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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalTime;

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
import org.web3j.protocol.core.Response;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthAccounts;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthBlock.TransactionResult;
import org.web3j.protocol.core.methods.response.EthBlockNumber;
import org.web3j.protocol.core.methods.response.EthGasPrice;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthGetBlockTransactionCountByNumber;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.EthTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.DefaultGasProvider;

import io.reactivex.disposables.Disposable;
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
	public List<TransactionReceipt> transferFunds(OtterCoin otterCoin, List<String> addressList, int amount) throws Exception {
		BigInteger bigIntegerAmount = BigInteger.valueOf(amount);
		List<TransactionReceipt> transactionReceiptList = new ArrayList<>();
		
		for (String address : addressList) {
		TransactionReceipt receipt = otterCoin.transfer(address, bigIntegerAmount).send();
		transactionReceiptList.add(receipt);
		}
		return transactionReceiptList;
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
	public boolean transferUsingCustomFromAddress(OtterCoin otterCoin, String fromAddress, String toAddress, int transferAmount, List<String> receiptList) throws IOException, InterruptedException, ExecutionException{
		
		Boolean success = true; 
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
		
		Response.Error error = transactionResponse.getError();
		String result = transactionResponse.getResult();
		String rawResponse = transactionResponse.getRawResponse();
		
		if (transactionResponse.getError() != null) { 
			success = false;
			return success;
		}
		//System.out.println(transactionHash + "Result " + result + "Raw Response " + rawResponse + "Error " + error.getMessage());
		receiptList.add(transactionHash);
		return success;
	}
	
	//spendAllowance() transfers allowance funds using a custom from address
		public boolean spendAllowance(OtterCoin otterCoin, String msgSenderAddress, String granterAddress, String toAddress, int transferAmount, List<String> receiptList) throws IOException, InterruptedException, ExecutionException{
			
			Boolean success = true; 
			BigInteger gasPrice = otterCoin.GAS_PRICE;
			BigInteger gasLimit = otterCoin.GAS_LIMIT;
			EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(msgSenderAddress, DefaultBlockParameterName.LATEST).send();
			BigInteger nonce = ethGetTransactionCount.getTransactionCount();
					
			Function function = new Function("transferFrom", // Function name
				 Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(granterAddress),
				 new org.web3j.abi.datatypes.Address(toAddress),		 
				 new org.web3j.abi.datatypes.Uint(BigInteger.valueOf(transferAmount))), // Function input parameters
				 Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {})
				 );
			String encodedFunction = FunctionEncoder.encode(function);
			
			Transaction transaction = Transaction.createFunctionCallTransaction(msgSenderAddress, nonce, gasPrice, gasLimit, otterCoin.getContractAddress(), 
				 encodedFunction);
			org.web3j.protocol.core.methods.response.EthSendTransaction transactionResponse = web3j.ethSendTransaction(transaction).sendAsync().get();
			String transactionHash = transactionResponse.getTransactionHash();
			
			Response.Error error = transactionResponse.getError();
			String result = transactionResponse.getResult();
			String rawResponse = transactionResponse.getRawResponse();
			
			if (transactionResponse.getError() != null) { 
				success = false;
				return success;
			}
			
			receiptList.add(transactionHash);
			System.out.println(transactionHash + "Result " + result + "Raw Response " + rawResponse);
			return success;
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
	
	/***Blockchain Visualization Methods***/
	public List<EthereumBlock> getAllBlocksFromBlockchain() throws IOException {
		
		List<EthereumBlock> ethereumBlocksList = new ArrayList<>();
		EthBlock firstBlock = web3j.ethGetBlockByNumber(DefaultBlockParameterName.EARLIEST, true).send();
		EthBlock lastBlock = web3j.ethGetBlockByNumber(DefaultBlockParameterName.LATEST, true).send();
		int start = firstBlock.getBlock().getNumber().intValue(); 
		int stop = lastBlock.getBlock().getNumber().intValue(); 
		
		for (int i = start; i < stop; i++) {
			
			BigInteger currentIndex = BigInteger.valueOf(i);
			EthBlock currentBlock = web3j.ethGetBlockByNumber(DefaultBlockParameter.valueOf(currentIndex), true).send();
			
			long id = currentBlock.getId();
			BigInteger blockNumber = currentBlock.getBlock().getNumber();
			BigInteger bigIntegerTime = currentBlock.getBlock().getTimestamp();
			String timestamp = new SimpleDateFormat("MM-dd-yy H:mm a").format(new Date(bigIntegerTime.intValue() * 1000L));
			BigInteger gasLimit = currentBlock.getBlock().getGasLimit();
			BigInteger gasUsed = currentBlock.getBlock().getGasUsed();
			BigInteger blockSize = currentBlock.getBlock().getSize();
			String receipts = currentBlock.getBlock().getReceiptsRoot();
			
			EthereumBlock newEthereumBlock = new EthereumBlock(id, blockNumber, timestamp, gasLimit, gasUsed, blockSize, receipts);
			ethereumBlocksList.add(newEthereumBlock);
			
		}
		return ethereumBlocksList;
		
	}
	
	
	
}

/***References:
 * https://www.baeldung.com/web3j
 * https://hackernoon.com/ethereum-token-development-using-java-and-web3j-an-overview-spas324r
 * https://github.com/web3j/web3j/issues/833
 * https://www.codota.com/code/java/methods/org.web3j.protocol.Web3j/ethGasPrice
 * https://stackoverflow.com/questions/25458832/how-can-i-convert-an-integer-e-g-19000101-to-java-util-date
 * 
 ***/
 