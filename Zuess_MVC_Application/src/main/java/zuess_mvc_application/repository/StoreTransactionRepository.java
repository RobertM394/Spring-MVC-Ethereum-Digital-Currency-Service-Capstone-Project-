package zuess_mvc_application.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import zuess_mvc_application.domain.*;

public interface StoreTransactionRepository extends JpaRepository<StoreTransaction, Long>{
	
	@Query(value="SELECT * FROM store_transactions WHERE user_id = 1? ORDER BY transaction_date DESC LIMIT 2?", nativeQuery=true)
	List<StoreTransaction> getMostRecentTransactionsByUserID(int user_id, int number_of_transactions);

}
