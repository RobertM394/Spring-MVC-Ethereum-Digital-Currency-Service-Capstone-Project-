/***
 * InventoryItem is a domain class representing an inventory item available for sale in the store.
 * The "scholarship_eligible" boolean attribute is used by the store to determine whether a purchase is available for 
 * scholarship funds use.
 * @Author Robert Meis
 */

package zuess_mvc_application.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.lang.Nullable;

@Entity
@Table(name="InventoryItem")
public class InventoryItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column
	boolean scholarship_eligible;
	
	@Column
	String name;
	
	@Column
	String type;
	
	@Column
	String description;

	@Column
	int price;
	
	@Column
	String image_source;
	
	public InventoryItem () { }

	public InventoryItem(boolean scholarship_eligible, String name, String type, String description, int price,
	      String image_source)
	{
		super();
		this.scholarship_eligible = scholarship_eligible;
		this.name = name;
		this.type = type;
		this.description = description;
		this.price = price;
		this.image_source = image_source;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public boolean isScholarship_eligible()
	{
		return scholarship_eligible;
	}

	public void setScholarship_eligible(boolean scholarship_eligible)
	{
		this.scholarship_eligible = scholarship_eligible;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public int getPrice()
	{
		return price;
	}

	public void setPrice(int price)
	{
		this.price = price;
	}

	public String getImage_source()
	{
		return image_source;
	}

	public void setImage_source(String image_source)
	{
		this.image_source = image_source;
	}

}
