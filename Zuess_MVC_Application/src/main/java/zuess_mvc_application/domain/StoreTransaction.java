package zuess_mvc_application.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Date;

@Entity
@Table(name="store_transactions")
public class StoreTransaction {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int id;
	
	int user_id;
	
	int transaction_total;
	
	int scholarship_funds_used;
	
	String items_list;
	
	java.sql.Date transaction_date;
	
	public StoreTransaction() { }

	public StoreTransaction(int user_id, int transaction_total, int scholarship_funds_used, String items_list,
	      Date transaction_date)
	{
		super();
		this.user_id = user_id;
		this.transaction_total = transaction_total;
		this.scholarship_funds_used = scholarship_funds_used;
		this.items_list = items_list;
		this.transaction_date = transaction_date;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public int getUser_id()
	{
		return user_id;
	}

	public void setUser_id(int user_id)
	{
		this.user_id = user_id;
	}

	public int getTransaction_total()
	{
		return transaction_total;
	}

	public void setTransaction_total(int transaction_total)
	{
		this.transaction_total = transaction_total;
	}

	public int getScholarship_funds_used()
	{
		return scholarship_funds_used;
	}

	public void setScholarship_funds_used(int scholarship_funds_used)
	{
		this.scholarship_funds_used = scholarship_funds_used;
	}

	public String getItems_list()
	{
		return items_list;
	}

	public void setItems_list(String items_list)
	{
		this.items_list = items_list;
	}

	public java.sql.Date getTransaction_date()
	{
		return transaction_date;
	}

	public void setTransaction_date(java.sql.Date transaction_date)
	{
		this.transaction_date = transaction_date;
	}

}




