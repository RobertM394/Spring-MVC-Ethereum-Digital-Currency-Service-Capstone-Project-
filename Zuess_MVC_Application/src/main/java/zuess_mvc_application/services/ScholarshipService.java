package zuess_mvc_application.services;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import zuess_mvc_application.domain.*;
import zuess_mvc_application.repository.ScholarshipRepository;

@Service
public class ScholarshipService {

	@Autowired
	ScholarshipRepository scholarshipRepository;
	
	@Autowired 
	BlockchainService blockchainService;
	
	@Autowired
	CustomUserDetailsService customUserDetailsService;
	
	//grantNewScholarship() Creates a new scholarship in the database and calls approveAllowance() to approve an allowance on the blockchain
	public TransactionReceipt grantNewScholarship(OtterCoin otterCoin, int recipient_id, String recipient_eth_id, int amount, java.sql.Date date_expires) throws Exception {
		
		Date date_granted = new Date(System.currentTimeMillis());
		Scholarship scholarship = new Scholarship(recipient_id, recipient_eth_id, amount, date_granted, date_expires);
		scholarshipRepository.save(scholarship);
		final TransactionReceipt receipt = blockchainService.approveAllowance(otterCoin, recipient_eth_id, amount);
		return receipt;
	}
	
	/***@Todo needs fix for duplicate scholarships in database***/
	public List<Scholarship> syncEthereumAndDatabaseAllowances(OtterCoin otterCoin, String admin_eth_address) throws Exception {
		List<Scholarship> scholarshipsList = getActiveScholarships();
		
		for (Scholarship scholarship : scholarshipsList) {
			int currentBalance = blockchainService.getAllowance(otterCoin, admin_eth_address, scholarship.getRecipient_eth_id());
			scholarship.setAmount(currentBalance);
			scholarshipRepository.updateScholarshipBalance(scholarship.getId(), currentBalance);
		}
		return scholarshipsList;
	}
	
	public List<Scholarship> getActiveScholarships() {
		List<Scholarship> scholarshipsList = new ArrayList<>();
		scholarshipsList = scholarshipRepository.getActiveScholarships();
		return scholarshipsList;
	}
	
	public List<Scholarship> getScholarshipsByUserId(int userId){
		List<Scholarship> scholarshipsList = new ArrayList<>();
		scholarshipsList = scholarshipRepository.getActiveScholarshipsByUserId(userId);
		return scholarshipsList;
	}
	
	public int getScholarshipBalanceFromBlockchain(OtterCoin otterCoin, String granterAddress, String recipientEmail) throws Exception {
		User user = customUserDetailsService.retrieveUserByEmail(recipientEmail);
		int scholarshipBalance = blockchainService.getAllowance(otterCoin, granterAddress, user.getEth_account_id());
		return scholarshipBalance;
	}
		
}
