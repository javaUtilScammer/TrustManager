import java.util.Date.*; 
import java.sql.*; 

public class Contribution
{
	private int id, state;
	private double contribution_score, score_validity, lat, lng;
	private Timestamp created_at; 
	private Account contributor; 
	public Contribution(int id, Account contributor, double contribution_score, double score_validity, Timestamp created_at, int state)
	{
		this.id=id; 
		this.contributor = contributor;
                this.contribution_score = contribution_score; 
                this.score_validity = score_validity; 
		this.created_at = created_at; 
		this.state = state; 
	}

	public int getId()
	{
		return id; 
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