package zuess_mvc_application.services;

import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import zuess_mvc_application.domain.CustomUserDetails;
import zuess_mvc_application.domain.OtterCoin;
import zuess_mvc_application.domain.User;
import zuess_mvc_application.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService{
	
	@Autowired 
	BlockchainService blockchainService; 
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(username);
		if (user == null) {
			throw new UsernameNotFoundException("No such user in database.");
		}
		return new CustomUserDetails(user);
	}
	
	public User retrieveUserByEmail(String email) {
		User user = userRepository.findByEmail(email);
		return user;
	}
	
	public User syncEthereumAndDatabaseUserBalances(OtterCoin otterCoin, User user) throws Exception {
		String ethAccountId = user.getEth_account_id();
		BigInteger bigIntegerBalance = blockchainService.getBalance(otterCoin, ethAccountId);
		double balance = bigIntegerBalance.doubleValue();
		user.setEth_account_balance(balance);
		userRepository.updateAccountBalance(ethAccountId, balance);
		return user;
	}
}
