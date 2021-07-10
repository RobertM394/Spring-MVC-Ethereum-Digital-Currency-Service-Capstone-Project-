package zuess_mvc_application.domain;

import java.util.ArrayList;
import java.util.List;

public class EthereumAccounts {
	
	private List<String> ethAccountsListCopy;
	private int numberOfAvailableAccounts;
	
	public EthereumAccounts() { }
	
	public EthereumAccounts(List<String> ethAccountsList) {
		super();
		this.ethAccountsListCopy = new ArrayList<String>(ethAccountsList);
		this.numberOfAvailableAccounts = ethAccountsListCopy.size();
	}
	
	public String assignNewEthereumAccount() {
		
		for (int i = 2; i < ethAccountsListCopy.size(); i++) {
			String assignedAccount = ethAccountsListCopy.get(i);
			ethAccountsListCopy.remove(i);
			numberOfAvailableAccounts -= 1;
			return assignedAccount;
		}
		return null;
	}

	public List<String> getEthAccountsList()
	{
		return ethAccountsListCopy;
	}

	public void setEthAccountsList(List<String> ethAccountsList)
	{
		this.ethAccountsListCopy = new ArrayList<String>(ethAccountsList);
	}

	public int getNumberOfAvailableAccounts()
	{
		return numberOfAvailableAccounts;
	}

	public void setNumberOfAvailableAccounts(int numberOfAvailableAccounts)
	{
		this.numberOfAvailableAccounts = numberOfAvailableAccounts;
	}
	
}
