import java.util.Date.*; 
import java.sql.*; 

public class Contribution
{
	private int contribution_id, state;
	private double contribution_score, score_validity, lat, lng;
	private Timestamp created_at; 
	private Account contributor; 
	public Contribution(int id, Account contributor, double contribution_score, double score_validity, Timestamp created_at, int state)
	{
		this.contribution_id=id; 
		this.contributor = contributor;
                this.contribution_score = contribution_score; 
                this.score_validity = score_validity; 
		this.created_at = created_at; 
		this.state = state; 
	}
	
	public boolean updateDB(Connection conn)
	{
	    try{
		Statement st = conn.createStatement();
		String update = "UPDATE Contributions"
			+ "SET contribution_score = " + contribution_score + ", score_validity = " + score_validity + ", state = " + state  
			+ "WHERE contribution_id = " + contribution_id; 
		
		return true; 
	    }catch(SQLException e)
	    {
		e.printStackTrace(); 
		return false; 
	    }
	}
	

	public int getId()
	{
		return contribution_id; 
	}

	public Account getContributor()
	{
		return contributor; 
	}

	public double getContributionScore()
	{
		return contribution_score;
	}

	public double getLat()
	{
		return lat;
	}

	public double getLng()
	{
		return lng; 
	}
}