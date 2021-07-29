/***
 * Inventory Service handles Store inventory item retrieval from the database
 * @Author Robert Meis
 */
package zuess_mvc_application.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zuess_mvc_application.domain.InventoryItem;
import zuess_mvc_application.repository.InventoryRepository;

@Service
public class InventoryService {
	
	@Autowired
	InventoryRepository inventoryRepository;
	
	public InventoryItem getInventoryItemById(int id) {
		InventoryItem item = inventoryRepository.getInventoryItemByID(id);
		return item; 
	}
	
	public List<InventoryItem> getAllInventoryItems(){
		List<InventoryItem> inventoryItemsList = inventoryRepository.getAllInventoryItems();
		return inventoryItemsList;
	}
}
