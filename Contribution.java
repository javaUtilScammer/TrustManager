import java.util.Date.*; 
import java.sql.Timestamp; 

public class Contribution
{
	private int id, contributor_id,state;
	private double contribution_score, lat, lng;
	Object[] data; 
	Timestamp created_at, last_updated_at; 
	public Contribution(int id, int contributor_id, Object[] data, double lat, double lng)
	{
		this.id=id; 
		this.contributor_id = contributor_id; 
		this.data = data;
		this.lat = lat;
		this.lng = lng; 
	}

	public int getId()
	{
		return id; 
	}

	public int getContributorId()
	{
		return contributor_id; 
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