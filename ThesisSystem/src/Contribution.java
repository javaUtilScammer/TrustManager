import java.util.Date.*; 
import java.sql.*; 

public class Contribution
{
	private int id, state;
	private double contribution_score, score_validity, lat, lng;
	private String message; 
	private Object[] data; 
	private Timestamp created_at, last_updated_at; 
	private Account contributor; 
	public Contribution(int id, String message, Object[] data, double lat, double lng, Account contributor)
	{
		this.id=id; 
		this.contributor = contributor;
		this.data = data;
		this.lat = lat;
		this.lng = lng; 
		this.message = message; 
                contribution_score = 50; 
                score_validity = 50; 
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

	public Object[] getData()
	{
		return data; 
	}
}