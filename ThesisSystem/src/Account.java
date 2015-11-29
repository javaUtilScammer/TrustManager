import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date.*;
import java.sql.Timestamp; 
import java.util.ArrayList;

public class Account extends Component
{
	private int account_id;
	ArrayList<Contribution> contributions;
	private String username;
	private Timestamp created_at, last_updated_at;
	private double trust_rating, trust_confidence;

	public Account(int account_id, String username, Timestamp created_at, Timestamp last_updated_at, double trust_rating, double trust_confidence)
	{
		this.account_id = account_id;
		this.username = username; 
		this.created_at = created_at;
		this.last_updated_at = last_updated_at; 
		this.trust_rating = trust_rating;
		this.trust_confidence = trust_confidence;
		type = 'a';
		contributions = new ArrayList<Contribution>();
	}
	
	public boolean updateDB(Connection conn)
	{
	    try{
		Statement st = conn.createStatement();
		String update = "UPDATE Accounts"
			+ "SET last_updated_at = \"" + last_updated_at + "\", trust_rating = " + trust_rating + ", trust_confidence = " + trust_confidence  
			+ "WHERE account_id = " + account_id; 
		
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

	public double getTrustConfidence()
	{
		return trust_confidence; 
	}

	public void setTrustRating(double tr, Connection conn){
		trust_rating = tr;
		updateDB(conn);
	}
        
        public void setTrustConfidence(double tr, Connection conn){
		trust_confidence = tr;
		updateDB(conn);
	}

	public void setTrustRating(double tr){
		trust_rating = tr;
	}
}