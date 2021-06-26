package zuess_mvc_application.domain;

import java.util.ArrayList;
import java.util.List;

public class EthereumAccounts {
	
	private List<String> ethAccountsList = new ArrayList<>();
	private int numberOfAvailableAccounts;
	
	public EthereumAccounts() { }
	
	public EthereumAccounts(List<String> ethAccountsList) {
		super();
		this.ethAccountsList = ethAccountsList;
		this.numberOfAvailableAccounts = ethAccountsList.size();
	}
	
	public String assignNewEthereumAccount() {
		
		for (int i = 1; i < ethAccountsList.size(); i++) {
			String assignedAccount = ethAccountsList.get(i);
			ethAccountsList.remove(i);
			numberOfAvailableAccounts -= 1;
			return assignedAccount;
		}
		return null;
	}

	public List<String> getEthAccountsList()
	{
		return ethAccountsList;
	}

	public void setEthAccountsList(List<String> ethAccountsList)
	{
		this.ethAccountsList = ethAccountsList;
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
