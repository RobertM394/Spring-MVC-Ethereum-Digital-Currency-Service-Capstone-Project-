package zuess_mvc_application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import zuess_mvc_application.domain.*;
import zuess_mvc_application.repository.*; 
import zuess_mvc_application.services.*;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

	
@SpringBootTest
public class InventoryServiceTest {
	
	@MockBean
	InventoryRepository inventoryRepository;
	
	//Class being tested
	@Autowired
	InventoryService inventoryService;
	
	@BeforeEach
	public void setUpEach() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void getInventoryItemByIdShouldReturnValidItem() {
		InventoryItem expected = new InventoryItem(true, "SQL Injections Attacks and Defense", "Textbook", "Author: Clarke", 100, "img");
		expected.setId(1);
		
		given(inventoryRepository.getInventoryItemByID(1)).willReturn(expected);
		InventoryItem result = inventoryService.getInventoryItemById(1);
		assertEquals(result, expected);
	}
	
	@Test
	public void getAllInventoryItemsShouldReturnValidItemsList() {
		List<InventoryItem> expectedList = new ArrayList<InventoryItem>();
		InventoryItem item = new InventoryItem(true, "SQL Injections Attacks and Defense", "Textbook", "Author: Clarke", 100, "img");
		item.setId(1);
		expectedList.add(item);
		
		given(inventoryRepository.getAllInventoryItems()).willReturn(expectedList);
		List<InventoryItem> resultList = inventoryService.getAllInventoryItems();
		assertEquals(resultList, expectedList);
	}
	
}
