package zuess_mvc_application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;


import static org.mockito.ArgumentMatchers.anyString;
import zuess_mvc_application.domain.InventoryItem;
import zuess_mvc_application.domain.OtterCoin;
import zuess_mvc_application.domain.User;
import zuess_mvc_application.repository.InventoryRepository;
import zuess_mvc_application.repository.UserRepository;
import zuess_mvc_application.services.BlockchainService;
import zuess_mvc_application.services.CustomUserDetailsService;
import zuess_mvc_application.services.InventoryService;

@SpringBootTest
public class CustomUserDetailsServiceTests {

	@MockBean
	UserRepository userRepository;

	//Class being tested
	@Autowired
	CustomUserDetailsService customUDService;
	
	@MockBean
	BlockchainService blockchainService;
	
	@BeforeEach
	public void setUpEach() {
		MockitoAnnotations.initMocks(this);
	}

	
	@Test
	public void shouldRetrieveUserByEmail() {
		
		User expected = new User("test@email.com","pw","test","lname","testAcctId",50.00,1);
		
		
		given(userRepository.findByEmail("test@email.com")).willReturn(expected);
		
		User result = customUDService.retrieveUserByEmail("test@email.com");	
		
		assertEquals(result, expected);
	
	}
	@Test
	public void userNull() {

		given(userRepository.findByEmail(anyString())).willReturn(null);
		
		User actual = customUDService.retrieveUserByEmail("test@email.com");
		
		assertNull(actual);
	}
	

}
