package zuess_mvc_application.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Date;

@Entity
@Table(name="scholarships")
public class Scholarship {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column
	private int recipient_id;
	
	@Column
	private String recipient_eth_id;
	
	@Column
	int amount;
	
	@Column
	java.sql.Date date_granted;
	
	@Column
	java.sql.Date date_expires;

	public Scholarship () { }

	public Scholarship(int recipient_id, String recipient_eth_id, int amount, Date date_granted, Date date_expires)
	{
		super();
		this.recipient_id = recipient_id;
		this.recipient_eth_id = recipient_eth_id;
		this.amount = amount;
		this.date_granted = date_granted;
		this.date_expires = date_expires;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public int getRecipient_id()
	{
		return recipient_id;
	}

	public void setRecipient_id(int recipient_id)
	{
		this.recipient_id = recipient_id;
	}

	public String getRecipient_eth_id()
	{
		return recipient_eth_id;
	}

	public void setRecipient_eth_id(String recipient_eth_id)
	{
		this.recipient_eth_id = recipient_eth_id;
	}

	public int getAmount()
	{
		return amount;
	}

	public void setAmount(int amount)
	{
		this.amount = amount;
	}

	public java.sql.Date getDate_granted()
	{
		return date_granted;
	}

	public void setDate_granted(java.sql.Date date_granted)
	{
		this.date_granted = date_granted;
	}

	public java.sql.Date getDate_expires()
	{
		return date_expires;
	}

	public void setDate_expires(java.sql.Date date_expires)
	{
		this.date_expires = date_expires;
	}

}
