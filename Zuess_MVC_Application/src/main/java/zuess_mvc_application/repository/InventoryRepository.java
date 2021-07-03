package zuess_mvc_application.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import zuess_mvc_application.domain.*;

public interface InventoryRepository extends JpaRepository<InventoryItem, Long> {
	
	@Query(value="SELECT * FROM InventoryItem WHERE id = ?1 LIMIT 1", nativeQuery=true)
	InventoryItem getInventoryItemByID(int id);

}
