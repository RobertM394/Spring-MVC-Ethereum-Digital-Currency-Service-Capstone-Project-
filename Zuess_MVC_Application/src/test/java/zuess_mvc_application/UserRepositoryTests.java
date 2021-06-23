package zuess_mvc_application;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import zuess_mvc_application.domain.User;
import zuess_mvc_application.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {
	@Autowired
	private TestEntityManager entityManager;
	
	@Autowired
	private UserRepository userRepo;
	
	@Test
	public void testCreateEntry() {
//		User(String email, String password, String first_name, String last_name, int eth_account_id,
//			      double eth_account_balance, int organization_id)
		User user = new User("test@email.com", "testPass", "testFName", "testLName", 1234, 1.4, 1);
		
		User savedUser = userRepo.save(user);
		
		User testUser = entityManager.find(User.class, savedUser.getId());
		
		assertThat (user.getEmail()).isEqualTo(testUser.getEmail());
	}
}
