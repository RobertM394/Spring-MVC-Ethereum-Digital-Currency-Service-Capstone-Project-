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
	
	@Query(value="SELECT * FROM scholarships", nativeQuery=true)
	List<Scholarship> getActiveScholarships();

}
