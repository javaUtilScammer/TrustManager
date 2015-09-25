import java.util.Date.*;
import java.sql.*; 

public class Evaluation
{
	int evaluation_id; 
	double rating; 
	Timestamp created_at;
	Account created_by;
	Contribution contribution; 
	public Evaluation(int evaluation_id, double rating, Timestamp created_at, Account created_by, Contribution contribution)
	{
		this.evaluation_id = evaluation_id;
		this.rating = rating;
		this.created_at = created_at; 
		this.created_by = created_by; 
		this.contribution = contribution; 
	}
	
	public boolean updateDB(Connection conn)
	{
	    try{
		Statement st = conn.createStatement();
		String update = "UPDATE Evaluations"
			+ "SET rating = " + rating
			+ "WHERE evaluation_id = " + evaluation_id; 
		
		return true; 
	    }catch(SQLException e)
	    {
		e.printStackTrace(); 
		return false; 
	    }
	}

	public int getId()
	{
		return evaluation_id; 
	}

	public double getRating()
	{
		return rating;
	}

	public Timestamp getCreatedAt()
	{
		return created_at; 
	}

	public Contribution getContribution()
	{
		return contribution; 
	}

	public Account getCreatedBy()
	{
		return created_by; 
	}
}