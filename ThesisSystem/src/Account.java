import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date.*;
import java.sql.Timestamp; 

public class Account
{
	private int account_id;
	private String username;
	private Timestamp created_at, last_updated_at;
	private double trust_rating, trust_validity;

	public Account(int account_id, String username, Timestamp created_at, Timestamp last_updated_at, double trust_rating, double trust_validity)
	{
		this.account_id = account_id;
		this.username = username; 
		this.created_at = created_at;
		this.last_updated_at = last_updated_at; 
		this.trust_rating = trust_rating;
		this.trust_validity = trust_validity;
	}
	
	public boolean updateDB(Connection conn)
	{
	    try{
		Statement st = conn.createStatement();
		String update; 
		
		return true; 
	    }catch(SQLException e)
	    {
		e.printStackTrace(); 
		return false; 
	    }
	}

	public int getId()
	{
		return account_id;
	}

	public String getUsername()
	{
		return username;
	}

	public Timestamp getCreatedAt()
	{
		return created_at; 
	}

	public Timestamp getUpdatedAt()
	{
		return last_updated_at; 
	}

	public double getTrustRating()
	{
		return trust_rating;
	}

	public double getTrustValidity()
	{
		return trust_validity; 
	}
}