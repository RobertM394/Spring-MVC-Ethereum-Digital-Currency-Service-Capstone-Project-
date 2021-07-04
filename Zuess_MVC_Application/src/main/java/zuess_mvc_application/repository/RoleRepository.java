package zuess_mvc_application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import zuess_mvc_application.domain.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
	@Query("SELECT u FROM Role u WHERE u.name = ?1")
	public Role findByName(String role);
}
