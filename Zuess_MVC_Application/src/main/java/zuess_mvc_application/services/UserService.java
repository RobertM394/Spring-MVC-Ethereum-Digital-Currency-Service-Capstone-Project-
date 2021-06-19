package zuess_mvc_application.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zuess_mvc_application.repository.*;
import zuess_mvc_application.domain.*;

@Service
public class UserService {
	
	@Autowired
	UserRepository userRepository;
	
	//persistNewUser()
	//
	//Persists new user to database and returns User object
	public User persistNewUser(User user) {
		userRepository.save(user);	
		return user;
	}
	
}
