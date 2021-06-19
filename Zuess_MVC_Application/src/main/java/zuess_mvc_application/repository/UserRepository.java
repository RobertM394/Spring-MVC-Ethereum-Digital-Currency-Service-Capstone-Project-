package zuess_mvc_application.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import zuess_mvc_application.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
	
}
