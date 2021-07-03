package zuess_mvc_application.services;

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
}
