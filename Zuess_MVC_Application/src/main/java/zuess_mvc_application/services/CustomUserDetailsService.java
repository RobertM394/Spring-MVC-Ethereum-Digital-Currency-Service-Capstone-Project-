package zuess_mvc_application.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import zuess_mvc_application.domain.CustomUserDetails;
import zuess_mvc_application.domain.User;
import zuess_mvc_application.repository.UserRepository;

public class CustomUserDetailsService implements UserDetailsService{
	
	@Autowired
	private UserRepository userRepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepo.findByEmail(username);
		if (user == null) {
			throw new UsernameNotFoundException("No such user in database.");
		}
		return new CustomUserDetails(user);
	}
}
