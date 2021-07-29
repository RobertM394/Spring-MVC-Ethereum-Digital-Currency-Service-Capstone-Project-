/***
 * Ethereum Block is a domain class object used for the Blockchain Visualization page. Each EthereumBlock object 
 * contains data representing one Ethereum block. Blockchain Data is retrieved using the BlockchainService method 
 * public List<EthereumBlock> getAllBlocksFromBlockchain()
 * @author Robert Meis
 */

package zuess_mvc_application.domain;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

public class EthereumBlock {
	
	long id;
	BigInteger blockNumber;
	String timestamp;
	BigInteger gasLimit;
	BigInteger gasUsed;
	BigInteger blockSize;
	String receipts;
	
	public EthereumBlock() { }

	public EthereumBlock(long id, BigInteger blockNumber, String timestamp, BigInteger gasLimit, BigInteger gasUsed,
	      BigInteger blockSize, String receipts)
	{
		super();
		this.id = id;
		this.blockNumber = blockNumber;
		this.timestamp = timestamp;
		this.gasLimit = gasLimit;
		this.gasUsed = gasUsed;
		this.blockSize = blockSize;
		this.receipts = receipts;
	}

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public BigInteger getBlockNumber()
	{
		return blockNumber;
	}

	public void setBlockNumber(BigInteger blockNumber)
	{
		this.blockNumber = blockNumber;
	}

	public String getTimestamp()
	{
		return timestamp;
	}

	public void setTimestamp(String timestamp)
	{
		this.timestamp = timestamp;
	}

	public BigInteger getGasLimit()
	{
		return gasLimit;
	}

	public void setGasLimit(BigInteger gasLimit)
	{
		this.gasLimit = gasLimit;
	}

	public BigInteger getGasUsed()
	{
		return gasUsed;
	}

	public void setGasUsed(BigInteger gasUsed)
	{
		this.gasUsed = gasUsed;
	}

	public BigInteger getBlockSize()
	{
		return blockSize;
	}

	public void setBlockSize(BigInteger blockSize)
	{
		this.blockSize = blockSize;
	}

	public String getReceipts()
	{
		return receipts;
	}

	public void setReceipts(String receipts)
	{
		this.receipts = receipts;
	}

}
