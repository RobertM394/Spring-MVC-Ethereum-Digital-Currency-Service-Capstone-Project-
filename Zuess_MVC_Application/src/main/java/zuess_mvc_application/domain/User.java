package zuess_mvc_application.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import zuess_mvc_application.validation.EmailUniqueConstraint;
import zuess_mvc_application.validation.PasswordValidConstraint;

@Entity
@Table(name="users")
public class User {
	
	@Id
	@Column(name= "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@EmailUniqueConstraint(message = "Please enter a unique Email.")
	@Pattern(regexp = "[A-Za-z0-9\\.-]+@[A-Za-z0-9]+\\.[A-Za-z]{3}", message = "Please enter a valid email.")
	@Size(max=50, message = "Email must be between 1 and 100 characters.")
	@Column(nullable = false, unique = true, length = 50)
	private String email;

	//TODO: Figure out display of which aspect of password is missing.
	
	//REQURIES Min length 8 Allows upper, lower, number (Only works as limit)
	@PasswordValidConstraint(message = "NOT VALID PASS")
	@Column(nullable = false, length = 100)
	private String password;
	
	@NotBlank(message = "First name is required.")
	@Size(max=50, message = "First name must be less than 50 characters.")
	@Column(nullable = false, length = 50, name = "first_name")
	private String first_name;
	
	@NotBlank(message = "Last name is required.")
	@Size(max=50, message = "Last name must be less than 50 characters.")
	@Column(nullable = false, length = 50, name = "last_name")
	private String last_name;
	
	@Column
	private String eth_account_id;
	
	@Column
	private double eth_account_balance;
	
	@Column
	private int organization_id;
	
	@ManyToMany(cascade= CascadeType.ALL, fetch = FetchType.EAGER, targetEntity = Role.class)
	@JoinTable(
			name = "user_roles",
			joinColumns = @JoinColumn(name = "id"),
			inverseJoinColumns = @JoinColumn(name = "role_id")
			)
	private List<Role> roles = new ArrayList<Role>();

	
	public User () { }

	public User(String email, String password, String first_name, String last_name, String eth_account_id,
	      double eth_account_balance, int organization_id)
	{
		super();
		this.email = email;
		this.password = password;
		this.first_name = first_name;
		this.last_name = last_name;
		this.eth_account_id = eth_account_id;
		this.eth_account_balance = eth_account_balance;
		this.organization_id = organization_id;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getFirst_name()
	{
		return first_name;
	}

	public void setFirst_name(String first_name)
	{
		this.first_name = first_name;
	}

	public String getLast_name()
	{
		return last_name;
	}

	public void setLast_name(String last_name)
	{
		this.last_name = last_name;
	}

	public String getEth_account_id()
	{
		return eth_account_id;
	}

	public void setEth_account_id(String eth_account_id)
	{
		this.eth_account_id = eth_account_id;
	}

	public double getEth_account_balance()
	{
		return eth_account_balance;
	}

	public void setEth_account_balance(double eth_account_balance)
	{
		this.eth_account_balance = eth_account_balance;
	}

	public int getOrganization_id()
	{
		return organization_id;
	}

	public void setOrganization_id(int organization_id)
	{
		this.organization_id = organization_id;
	}
	
	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
	
	public String getFull_name()
	{
		return first_name + " " + last_name;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		long temp;
		temp = Double.doubleToLongBits(eth_account_balance);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((eth_account_id == null) ? 0 : eth_account_id.hashCode());
		result = prime * result + ((first_name == null) ? 0 : first_name.hashCode());
		result = prime * result + id;
		result = prime * result + ((last_name == null) ? 0 : last_name.hashCode());
		result = prime * result + organization_id;
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (email == null)
		{
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (Double.doubleToLongBits(eth_account_balance) != Double.doubleToLongBits(other.eth_account_balance))
			return false;
		if (eth_account_id == null)
		{
			if (other.eth_account_id != null)
				return false;
		} else if (!eth_account_id.equals(other.eth_account_id))
			return false;
		if (first_name == null)
		{
			if (other.first_name != null)
				return false;
		} else if (!first_name.equals(other.first_name))
			return false;
		if (id != other.id)
			return false;
		if (last_name == null)
		{
			if (other.last_name != null)
				return false;
		} else if (!last_name.equals(other.last_name))
			return false;
		if (organization_id != other.organization_id)
			return false;
		if (password == null)
		{
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		return true;
	}
	
}
