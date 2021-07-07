package zuess_mvc_application.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import zuess_mvc_application.domain.*;

@Repository
public interface ScholarshipRepository extends JpaRepository<Scholarship, Long> {
	
	@Query(value="SELECT * FROM scholarships GROUP BY recipient_eth_id", nativeQuery=true)
	List<Scholarship> getActiveScholarships();
	
	@Query(value="SELECT * FROM scholarships WHERE recipient_id = ?1", nativeQuery=true)
	List<Scholarship> getActiveScholarshipsByUserId(int id);
	
	@Transactional
	@Modifying
	@Query(value="UPDATE scholarships SET amount = ?2 WHERE id = ?1", nativeQuery=true)
	void updateScholarshipBalance(int scholarshipId, int updatedBalance);

}
