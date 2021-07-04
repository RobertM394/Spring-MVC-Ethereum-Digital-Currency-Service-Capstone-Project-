package zuess_mvc_application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import zuess_mvc_application.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	@Query("SELECT u FROM User u WHERE u.email = ?1")
	public User findByEmail(String email);
	
	//TODO: reevaluate whether this is actually needed?
	@Query("SELECT u FROM User u WHERE u.email = :email")
	public User getUserByEmail(@Param("email") String email);
	
	@Modifying
	@Transactional
	@Query(value="UPDATE users SET eth_account_balance = ?2 WHERE email = ?1", nativeQuery=true)
	public void updateAccountBalance(String email, double balance);
}
